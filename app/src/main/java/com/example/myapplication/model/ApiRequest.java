package com.example.myapplication.model;

import java.util.List;

public class ApiRequest {
    private String model = "deepseek-chat";
    private List<Message> messages;
    private double temperature = 0.7;
    private int max_tokens = 2000;

    public ApiRequest(List<Message> messages) {
        this.messages = messages;
    }

    // Getters and Setters
    public String getModel() { return model; }
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
    public double getTemperature() { return temperature; }
    public int getMax_tokens() { return max_tokens; }
}
