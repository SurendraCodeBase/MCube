package com.mcube.app.tabs.followup;

import java.util.Date;


public class FollowUpData {
    private String callId;
    private String callFrom;
    private String dataId;
    private String callerName;
    private String groupName;
    private Date callTime;
    private String status;

    public FollowUpData(String callId, String callFrom, String dataId, String callerName, String groupName, Date callTime, String status) {
        this.callId = callId;
        this.callFrom = callFrom;
        this.dataId = dataId;
        this.callerName = callerName;
        this.groupName = groupName;
        this.callTime = callTime;
        this.status = status;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getCallFrom() {
        return callFrom;
    }

    public void setCallFrom(String callFrom) {
        this.callFrom = callFrom;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getCallTime() {
        return callTime;
    }

    public void setCallTime(Date callTime) {
        this.callTime = callTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
