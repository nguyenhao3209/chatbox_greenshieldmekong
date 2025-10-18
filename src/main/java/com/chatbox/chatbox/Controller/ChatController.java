package com.chatbox.chatbox.Controller;

import com.chatbox.chatbox.config.TopicConfig;
import com.chatbox.chatbox.dto.ChatRequest;
import com.chatbox.chatbox.model.ConversationSession;
import com.chatbox.chatbox.service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
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

    @PostMapping(value = "/message", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String chatWithImage(
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "image", required = false) MultipartFile image,
            HttpSession session
    ) {
        try {
            String topic = (String) session.getAttribute("topic");
            if (topic == null) return "Please select a topic first.";

            if ((message == null || message.isBlank()) && (image == null || image.isEmpty())) {
                return "Message or image cannot be empty.";
            }

            ConversationSession convo = (ConversationSession) session.getAttribute("conversation");
            if (convo == null || !topic.equals(convo.getTopic())) {
                convo = new ConversationSession(topic);
                session.setAttribute("conversation", convo);
            }

            String userMessage = (message != null && !message.isBlank()) ? message : "[Image uploaded]";
            convo.addMessage("User", userMessage);

            String reply;
            ChatRequest chatRequest = new ChatRequest(userMessage, topic);

            if (image != null && !image.isEmpty()) {
                System.out.println("✅ Image received: " + image.getOriginalFilename() + " (" + image.getSize() + " bytes)");
                String imageBase64 = Base64.getEncoder().encodeToString(image.getBytes());
                reply = chatService.chatWithImage(chatRequest, convo, imageBase64);
            } else {
                reply = chatService.chatWithKnowledge(chatRequest, convo);
            }

            convo.addMessage("Gemini", reply);
            session.setAttribute("conversation", convo);

            return reply;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/topics")
    public Map<String, String> getChatTopics() {
        return topicConfig.getAllTopics();
    }
}
