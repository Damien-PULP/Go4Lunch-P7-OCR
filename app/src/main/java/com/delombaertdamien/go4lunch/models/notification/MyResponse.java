package com.delombaertdamien.go4lunch.models.notification;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class MyResponse implements Serializable {


    private int multicastId;
    private int success;
    private int failure;
    private int canonicalIds;
    private List<Result> results;

    public MyResponse() {
        super();
    }

    public MyResponse(int multicastId, int success, int failure, int canonicalIds, List<Result> results) {
        this.multicastId = multicastId;
        this.success = success;
        this.failure = failure;
        this.canonicalIds = canonicalIds;
        this.results = results;
    }

    public int getMulticastId() {
        return multicastId;
    }

    public void setMulticastId(int multicastId) {
        this.multicastId = multicastId;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonicalIds() {
        return canonicalIds;
    }

    public void setCanonicalIds(int canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @NotNull
    @Override
    public String toString() {
        return "MyResponse{" +
                "multicastId=" + multicastId +
                ", success=" + success +
                ", failure=" + failure +
                ", canonicalIds=" + canonicalIds +
                ", results=" + results +
                '}';
    }
}