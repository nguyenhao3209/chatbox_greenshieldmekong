package com.chatbox.chatbox.config;

import org.springframework.stereotype.Component;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class TopicConfig {

    private final Map<String, String> topicMap = new LinkedHashMap<>();

    public TopicConfig() {
        topicMap.put("company", "About GreenShield Mekong: mission, vision, and values.");
        topicMap.put("products", "Handcrafted products and eco-friendly materials.");
        topicMap.put("order", "How to place an order online.");
        topicMap.put("shipping", "Delivery time, cost, and tracking info.");
        topicMap.put("payment", "Payment methods and refund policy.");
        topicMap.put("contact", "Hotline, email, and social media contact.");
        topicMap.put("sustainability", "Sustainability and environmental commitment.");
        topicMap.put("feedback", "How to send feedback or complaints.");
        topicMap.put("default", "General information about GreenShield Mekong.");
    }

    public String getTopicContext(String topic) {
        return topicMap.getOrDefault(topic, topicMap.get("default"));
    }

    public Map<String, String> getAllTopics() {
        return topicMap;
    }
}
