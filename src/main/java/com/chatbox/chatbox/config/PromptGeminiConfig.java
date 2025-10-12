package com.chatbox.chatbox.config;

import org.springframework.stereotype.Component;

@Component
public class PromptGeminiConfig {

    // Prompt mẫu, có thể chỉnh wording tùy theo yêu cầu
    private static final String PROMPT_TEMPLATE = """
        You are the assistant for GreenShield Mekong, a sustainable handicraft brand.
        Use the following knowledge and conversation history to answer accurately in Vietnamese.

        - Keep your responses short and concise (1–3 sentences or more if need).
        - Do not repeat the user's question.

        Topic: %s
        Knowledge:
        %s

        Conversation so far:
        %s

        New message:
        %s
        """;

    public String buildPrompt(String topicContext, String knowledgeContext, String conversationHistory, String userMessage) {
        return String.format(PROMPT_TEMPLATE, topicContext, knowledgeContext, conversationHistory, userMessage);
    }
}
