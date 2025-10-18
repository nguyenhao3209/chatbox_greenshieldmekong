package com.chatbox.chatbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConversationSession {
    private String topic;
    private List<String> messages = new ArrayList<>();

    public ConversationSession(String topic) {
        this.topic = topic;
    }

    public void addMessage(String role, String content) {
        messages.add(role + ": " + content);
    }

    public String getConversationHistory() {
        return String.join("\n", messages);
    }
}
