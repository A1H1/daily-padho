package in.devco.dailypadho.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SourceResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("sources")
    private List<Source> sources;
}
