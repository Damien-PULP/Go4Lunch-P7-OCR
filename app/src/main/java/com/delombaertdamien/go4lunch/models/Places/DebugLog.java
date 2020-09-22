package com.delombaertdamien.go4lunch.models.Places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DebugLog {

    @SerializedName("line")
    @Expose
    private List<Object> line = null;

    public List<Object> getLine() {
        return line;
    }

    public void setLine(List<Object> line) {
        this.line = line;
    }

}
