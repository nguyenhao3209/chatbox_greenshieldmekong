package com.chatbox.chatbox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
    private String message; // Nội dung người dùng nhập
    private String topic;   // Chủ đề người dùng chọn (vd: "products", "shipping", ...)

    // Constructor rỗng (cần thiết cho Spring hoặc Jackson khi deserialize JSON)
    public ChatRequest() {}

    // Constructor đầy đủ
    public ChatRequest(String message, String topic) {
        this.message = message;
        this.topic = topic;
    }

    // Constructor chỉ có message (trường hợp không có topic)
    public ChatRequest(String message) {
        this.message = message;
    }

    // Getter và Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    // toString để hỗ trợ debug/log dễ hơn
    @Override
    public String toString() {
        return "ChatRequest{" +
                "message='" + message + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
