package com.example.myapplication.model;

public class Message {
    private String role;
    private String content;

    // 添加构造函数
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    // Getter 方法
    public String getRole() { return role; }
    public String getContent() { return content; }

    // 可选：添加 Setter 方法
    public void setRole(String role) { this.role = role; }
    public void setContent(String content) { this.content = content; }
}