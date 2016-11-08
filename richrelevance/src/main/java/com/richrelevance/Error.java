package com.richrelevance;

public class Error {

    public enum ErrorType {
        Unknown,
        SdkNotConfigured,
        InvalidArguments,
        CannotParseResponse,
        ApiError,
        HttpError
    }

    private ErrorType type;
    private String message;

    public Error(ErrorType type, String message) {
        this.type = type;
        this.message = message;
    }

    public ErrorType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
