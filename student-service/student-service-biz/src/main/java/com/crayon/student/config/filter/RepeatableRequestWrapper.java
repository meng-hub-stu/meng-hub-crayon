package com.crayon.student.config.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.*;

/**
 * @author Mengdl
 * @date 2025/04/16
 */
public class RepeatableRequestWrapper extends ContentCachingRequestWrapper {

    public RepeatableRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 首次就缓冲数据
        IOUtils.copy(super.getInputStream(), new ByteArrayOutputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(getContentAsByteArray()));
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void mark(int readlimit) {
                inputStream.mark(readlimit);
            }

            @Override
            public void reset() throws IOException {
                inputStream.reset();
            }

            @Override
            public boolean markSupported() {
                return inputStream.markSupported();
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

}
