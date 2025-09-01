package com.example.ai.config;


import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {

    /**
     * 记忆存储
     */
    @Resource
    private ChatMemoryRepository chatMemoryRepository;

    /**
     * 初始化一个 ChatMemory 实例，并注入到 Spring 容器中
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(50) // 设置最大记忆消息数为10条
                .build();
    }
}
