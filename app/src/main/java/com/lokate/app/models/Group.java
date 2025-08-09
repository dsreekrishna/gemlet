package com.lokate.app.models;

import com.google.gson.annotations.SerializedName;

public class Group {
    @SerializedName("group_id")
    private int groupId;
    
    @SerializedName("group_display_name")
    private String groupDisplayName;
    
    @SerializedName("created_on")
    private String createdOn;
    
    @SerializedName("updated_on")
    private String updatedOn;

    public int getGroupId() {
        return groupId;
    }

    public String getGroupDisplayName() {
        return groupDisplayName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }
}
