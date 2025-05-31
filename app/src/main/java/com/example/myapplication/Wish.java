package com.example.myapplication;

public class Wish {
    private String id;
    private String content;
    private String constellation;
    private String qq;
    private long timestamp;

    // 必须有无参构造函数
    public Wish() {}

    // getters 和 setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getConstellation() { return constellation; }
    public void setConstellation(String constellation) { this.constellation = constellation; }
    public String getQq() { return qq; }
    public void setQq(String qq) { this.qq = qq; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}