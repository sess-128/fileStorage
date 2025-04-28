package com.rrtyui.filestorage.minio.util;

import com.rrtyui.filestorage.exception.InvalidPathException;
import com.rrtyui.filestorage.security.MyUserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MinioUtils {
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
        return path.endsWith("/");
    }

    public String createFileName(String path) {
        path = path.replaceAll("/+$", "");
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public String createZipName(String path) {
        return createFileName(path) + ".zip";
    }

    public String getCurrentUserPath(MyUserDetails userDetails) {
        return "user-%s-files/".formatted(userDetails.getMyUser().getId());
    }

    public boolean isRenameOperation(String from, String to) {
        if (isDirectoryPath(from) != isDirectoryPath(to)) {
            return false;
        }
        return getParentPrefix(from).equals(getParentPrefix(to));
    }

    public boolean isMoveOperation(String from, String to) {
        if (isDirectoryPath(from) != isDirectoryPath(to)) {
            return false;
        }
        return getLastSegmentPrefix(from).equals(getLastSegmentPrefix(to));
    }

    public String getContentType(String filename) {
        try {
            String type = Files.probeContentType(Path.of(filename));
            return type != null ? type : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    public void validatePath(String path) {
        Pattern pattern = Pattern.compile(REG_EXP);
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            throw new InvalidPathException("Invalid path");
        }
    }
}
