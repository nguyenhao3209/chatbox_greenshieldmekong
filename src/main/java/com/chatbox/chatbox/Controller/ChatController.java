package com.chatbox.chatbox.Controller;

import com.chatbox.chatbox.config.TopicConfig;
import com.chatbox.chatbox.dto.ChatRequest;
import com.chatbox.chatbox.model.ConversationSession;
import com.chatbox.chatbox.service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;
    private final TopicConfig topicConfig;

    public ChatController(ChatService chatService, TopicConfig topicConfig) {
        this.chatService = chatService;
        this.topicConfig = topicConfig;
    }

    @PostMapping("/select-topic")
    public String selectTopic(@RequestBody Map<String, String> body, HttpSession session) {
        String topic = body.get("topic");
        session.setAttribute("topic", topic);
        return "You are now chatting about: " + topicConfig.getTopicContext(topic);
    }

//    @PostMapping("/message")
//    public String chat(@RequestBody ChatRequest chatRequest, HttpSession session) {
//        String topic = (String) session.getAttribute("topic");
//        if (topic != null) {
//            chatRequest.setTopic(topic);
//        }
//        return chatService.chatWithKnowledge(chatRequest);
//    }

    @PostMapping(value = "/message", consumes = {"multipart/form-data", "application/json"})
    public String chat(
            @RequestPart(value = "message", required = false) String message,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestBody(required = false) ChatRequest jsonBody,
            HttpSession session
    ) throws IOException {

        String topic = (String) session.getAttribute("topic");
        if (topic == null) {
            return "Please select a topic first using /select-topic.";
        }

        // Xác định nội dung tin nhắn
        String userMessage = (message != null) ? message
                : (jsonBody != null ? jsonBody.getMessage() : null);
        if (userMessage == null || userMessage.isBlank()) {
            return "Message cannot be empty.";
        }

        // Lấy hoặc tạo ConversationSession
        ConversationSession convo = (ConversationSession) session.getAttribute("conversation");
        if (convo == null || !topic.equals(convo.getTopic())) {
            convo = new ConversationSession(topic);
            session.setAttribute("conversation", convo);
        }

        convo.addMessage("User", userMessage);

        String reply;

        if (image != null && !image.isEmpty()) {
            String imageBase64 = Base64.getEncoder().encodeToString(image.getBytes());
            ChatRequest chatRequest = new ChatRequest(userMessage, topic);
            reply = chatService.chatWithImage(chatRequest, convo, imageBase64);
        } else {
            ChatRequest chatRequest = new ChatRequest(userMessage, topic);
            reply = chatService.chatWithKnowledge(chatRequest, convo);
        }

        convo.addMessage("Gemini", reply);
        session.setAttribute("conversation", convo);

        return reply;
    }

    @GetMapping("/topics")
    public Map<String, String> getChatTopics() {
        return topicConfig.getAllTopics();
    }
}
