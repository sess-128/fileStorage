package com.rrtyui.filestorage.minio.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class WebUtil {
    public String getContentType(String filename) {
        try {
            String type = Files.probeContentType(Path.of(filename));
            return type != null ? type : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }
    public HttpServletResponse getPreparedResponse(boolean isDirectory, String name) {
        HttpServletResponse currentHttpResponse = getCurrentHttpResponse();
        prepareServletResponse(isDirectory, name, currentHttpResponse);
        return currentHttpResponse;
    }

    private void prepareServletResponse(boolean isDirectory, String name, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
        if (isDirectory){
            httpServletResponse.setContentType("application/zip");
        } else {
            httpServletResponse.setContentType("application/octet-stream");
        }
    }

    private HttpServletResponse getCurrentHttpResponse() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("No current request attributes found");
        }

        return attributes.getResponse();
    }
}
