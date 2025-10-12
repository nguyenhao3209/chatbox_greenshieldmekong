package com.chatbox.chatbox.service;

import com.chatbox.chatbox.model.Product;
import com.chatbox.chatbox.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class KnowledgeLoaderService {

    @Autowired
    private ProductRepository productRepository;

    public String loadKnowledge() {
        StringBuilder knowledgeBuilder = new StringBuilder();

        // Đọc nội dung từ file TXT
        try {
            String fileContent = Files.readString(Paths.get("src/main/resources/templates/greenshield_resource.txt"));
            knowledgeBuilder.append("=== COMPANY PROFILE ===\n").append(fileContent).append("\n\n");
        } catch (IOException e) {
            knowledgeBuilder.append("⚠️ Could not read knowledge file.\n");
        }

        // Đọc dữ liệu sản phẩm từ database
        List<Product> products = productRepository.findAll();

        knowledgeBuilder.append("=== PRODUCT CATALOG ===\n");
        for (Product p : products) {
            knowledgeBuilder
                    .append("Product: ").append(p.getName()).append("\n")
                    .append("Category: ").append(p.getCategory()).append("\n")
                    .append("Material: ").append(p.getMaterial()).append("\n")
                    .append("Price: ").append(p.getPrice()).append("\n")
                    .append("Description: ").append(p.getDescription()).append("\n\n");
        }

        return knowledgeBuilder.toString();
    }
}