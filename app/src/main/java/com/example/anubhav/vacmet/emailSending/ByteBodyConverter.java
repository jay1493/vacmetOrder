package com.example.anubhav.vacmet.emailSending;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * Created by anubhav on 26/4/16.
 */
public class ByteBodyConverter implements DataSource {

    private byte[] bodyBytes;
    private String bodyMimeType;

    public ByteBodyConverter(byte[] bodyBytes, String bodyMimeType) {
        super();
        this.bodyBytes = bodyBytes;
        this.bodyMimeType = bodyMimeType;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bodyBytes);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public String getContentType() {
        if(bodyMimeType==null){
            return "application/octet-stream";
        }
        return bodyMimeType;
    }

    @Override
    public String getName() {
        return null;
    }
}
