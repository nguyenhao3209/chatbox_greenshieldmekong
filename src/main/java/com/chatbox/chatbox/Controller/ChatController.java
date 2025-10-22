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

    @PostMapping(value = "/message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String chatOnlyMessage(@RequestBody Map<String, String> payload, HttpSession session) {
        try {
            String message = payload.get("message");
            String topic = (String) session.getAttribute("topic");
            if (topic == null) return "Please select a topic first.";
            if (message == null || message.isBlank()) return "Message cannot be empty.";

            ConversationSession convo = (ConversationSession) session.getAttribute("conversation");
            if (convo == null || !topic.equals(convo.getTopic())) {
                convo = new ConversationSession(topic);
                session.setAttribute("conversation", convo);
            }

            convo.addMessage("User", message);

            ChatRequest chatRequest = new ChatRequest(message, topic);
            String reply = chatService.chatWithKnowledge(chatRequest, convo);

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
