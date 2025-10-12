package com.chatbox.chatbox.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; // link ảnh (ví dụ: /uploads/banana-box1.jpg)

    @Column(nullable = false)
    private boolean mainImage = false; // ✅ Ảnh chính của sản phẩm
    // Khóa ngoại trỏ về Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
