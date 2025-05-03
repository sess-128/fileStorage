package com.rrtyui.filestorage.minio.util;

import com.rrtyui.filestorage.exception.InvalidPathException;
import com.rrtyui.filestorage.security.MyUserDetails;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class MinioUtil {
    private static final String REG_EXP = "(//|\\.\\.|--|-$|\\.$)";

    public String normalizePath(String path) {
        if (path == null || path.isEmpty() || path.equals("/")) {
            return "";
        }
        path = path.replaceFirst("^/", "");
        if (!path.endsWith("/")) {
            path += "/";
        }
        return path;
    }

    public String getParentPrefix(String path) {
        String normalizedPath = path.endsWith("/")
                ? path.substring(0, path.length() - 1)
                : path;

        int lastSlashIndex = normalizedPath.lastIndexOf('/');

        return lastSlashIndex >= 0
                ? normalizedPath.substring(0, lastSlashIndex + 1)
                : "";
    }

    public String getLastSegmentPrefix(String path) {
        String normalizedPath = path.endsWith("/")
                ? path.substring(0, path.length() - 1)
                : path;

        int lastSlashIndex = normalizedPath.lastIndexOf('/');
        String segment = normalizedPath.substring(lastSlashIndex + 1);
        
        return isDirectoryPath(path) ? segment + "/" : segment;
    }

    public boolean isDirectoryPath(String path) {
        return path.isEmpty() || path.endsWith("/");
    }

    public String createFileName(String path) {
        path = path.replaceAll("/+$", "");
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public String createZipName(String path) {
        return createFileName(path) + ".zip";
    }

    public String getCurrentUserPath() {
        MyUserDetails currentUser = getCurrentUser();
        return "user-%s-files/".formatted(currentUser.getMyUser().getId());
    }

    private MyUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        return (MyUserDetails) principal;
    }

    private boolean isRenameOperation(String from, String to) {
        if (isDirectoryPath(from) != isDirectoryPath(to)) {
            return false;
        }
        return getParentPrefix(from).equals(getParentPrefix(to));
    }

    private boolean isMoveOperation(String from, String to) {
        if (isDirectoryPath(from) != isDirectoryPath(to)) {
            return false;
        }
        return getLastSegmentPrefix(from).equals(getLastSegmentPrefix(to));
    }

    public void validatePath(String path) {
        Pattern pattern = Pattern.compile(REG_EXP);
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            throw new InvalidPathException("Invalid path");
        }
    }

    public boolean shouldSkip(Item item, String relativePath, String requestedPath) {
        boolean emptyDirectories = !item.isDir() && item.size() == 0 && relativePath.endsWith("/");
        boolean currentDirectory = relativePath.isEmpty() || relativePath.equals(requestedPath);

        return currentDirectory || emptyDirectories;
    }

    public void validateMove(String from, String to) {
        if (from.equals(to)) {
            throw new IllegalArgumentException("Source and destination paths are identical");
        }

        if (to.startsWith(from)) {
            throw new IllegalArgumentException("You can't move a folder inside yourself");
        }
    }

    public String defineOperation(String from, String to) {
        if (isRenameOperation(from, to)) {
            return to;
        }
        if (isMoveOperation(from, to)) {
            String originalFileName = getLastSegmentPrefix(from);
            return getParentPrefix(to) + originalFileName;
        }
        throw new UnsupportedOperationException("Combined rename+move operations are not supported");
    }

    public String getResourceNameByItemType(Item item) {
        String fullObjectName = item.objectName();
        int userPathLength = getCurrentUserPath().length();
        String relativePath = fullObjectName.substring(userPathLength);

        boolean isDirectory = item.isDir() || isDirectoryPath(fullObjectName);

        return isDirectory
                ? getLastSegmentPrefix(relativePath)
                : createFileName(relativePath);
    }

    public String getRelativePathByItem(Item item) {
        String fullObjectName = item.objectName();
        int userPathLength = getCurrentUserPath().length();

        return fullObjectName.substring(userPathLength);
    }

    public boolean isMatchQuery(String name, String query) {
        return name.toLowerCase().contains(query.toLowerCase());
    }
}
