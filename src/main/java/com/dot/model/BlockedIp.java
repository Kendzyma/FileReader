package com.dot.model;

public class BlockedIp {

    private String ip;
    private int requestNumber;
    private String comment;

    public BlockedIp() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "BlockedIp{" +
                "ip='" + ip + '\'' +
                ", requestNumber=" + requestNumber +
                ", comment='" + comment + '\'' +
                '}';
    }
}
