package com.bmathias.go4lunch_.data.network.model;

public class DataResult<T> {
    private T data;
    private Throwable error;

    public DataResult(T data) {
        this.data = data;
    }

    public DataResult(Throwable error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return data != null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
