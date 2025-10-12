package com.chatbox.chatbox.service;

import com.chatbox.chatbox.config.PromptGeminiConfig;
import com.chatbox.chatbox.config.TopicConfig;
import com.chatbox.chatbox.dto.ChatRequest;
import com.chatbox.chatbox.model.ConversationSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final GeminiService geminiService;

    @Autowired
    private KnowledgeLoaderService knowledgeLoaderService;

    @Autowired
    private TopicConfig topicConfig;

    @Autowired
    private PromptGeminiConfig promptGeminiConfig;

    public ChatService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * Chat bình thường (text only)
     */
    public String chatWithKnowledge(ChatRequest request, ConversationSession convo) {
        String knowledgeContext = knowledgeLoaderService.loadKnowledge();
        String conversationHistory = convo.getConversationHistory();

        String topic = (convo.getTopic() != null) ? convo.getTopic().trim().toLowerCase() : "";
        String topicContext = topicConfig.getTopicContext(topic);

        String prompt = promptGeminiConfig.buildPrompt(
                topicContext,
                knowledgeContext,
                conversationHistory,
                request.getMessage()
        );

        return geminiService.generateContent(prompt);
    }

    /**
     * Chat bằng hình ảnh (image + text)
     */
    public String chatWithImage(ChatRequest request, ConversationSession convo, String imageBase64) {
        String knowledgeContext = knowledgeLoaderService.loadKnowledge();
        String conversationHistory = convo.getConversationHistory();

        String topic = (convo.getTopic() != null) ? convo.getTopic().trim().toLowerCase() : "";
        String topicContext = topicConfig.getTopicContext(topic);

        String prompt = promptGeminiConfig.buildPrompt(
                topicContext,
                knowledgeContext,
                conversationHistory,
                request.getMessage()
        );

        return geminiService.generateContentWithImage(prompt, imageBase64);
    }
}
