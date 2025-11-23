package com.eps.module.common.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Wrapper to cache the response body so it can be logged and still sent to client.
 */
public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedBody = new ByteArrayOutputStream();
    private ServletOutputStream outputStream;
    private PrintWriter writer;

    public CachedBodyHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = new CachedBodyServletOutputStream(getResponse().getOutputStream(), cachedBody);
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(cachedBody, getCharacterEncoding()));
        }
        return writer;
    }

    /**
     * Get the cached response body as a string.
     */
    public String getBody() {
        return cachedBody.toString();
    }

    /**
     * Get the cached response body as bytes.
     */
    public byte[] getBodyBytes() {
        return cachedBody.toByteArray();
    }

    /**
     * Custom ServletOutputStream that writes to both the original response and cached buffer.
     */
    private static class CachedBodyServletOutputStream extends ServletOutputStream {
        private final ServletOutputStream originalOutputStream;
        private final ByteArrayOutputStream cachedBody;

        public CachedBodyServletOutputStream(ServletOutputStream originalOutputStream, ByteArrayOutputStream cachedBody) {
            this.originalOutputStream = originalOutputStream;
            this.cachedBody = cachedBody;
        }

        @Override
        public void write(int b) throws IOException {
            originalOutputStream.write(b);
            cachedBody.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            originalOutputStream.write(b);
            cachedBody.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            originalOutputStream.write(b, off, len);
            cachedBody.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            originalOutputStream.flush();
        }

        @Override
        public void close() throws IOException {
            originalOutputStream.close();
        }

        @Override
        public boolean isReady() {
            return originalOutputStream.isReady();
        }

        @Override
        public void setWriteListener(WriteListener listener) {
            originalOutputStream.setWriteListener(listener);
        }
    }
}
