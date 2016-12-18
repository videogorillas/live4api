package io.live4.apiclient.internal;

import java.io.IOException;

import com.squareup.okhttp.Response;

public class UnhandledResponseException extends IOException {
    private static final long serialVersionUID = 1L;
    private Response r;

    public UnhandledResponseException(Response r) {
        this.r = r;
    }

    public Response getResponse() {
        return r;
    }

    @Override
    public String getMessage() {
        return String.valueOf(r);
    }
}
