package com.example.ai.config;

import com.example.ai.advisor.ZhipuLoggerAdvisor;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Resource
    private ChatMemory chatMemory;

    /**
     * 初始化 ChatClient 客户端
     * @param chatModel
     * @return
     */
    @Bean
    public ChatClient chatClient(ZhiPuAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个电商平台的智能助手，请根据用户的提问，结合电商平台的业务逻辑，给出专业、准确、简洁的回答。")
                .defaultAdvisors(new SimpleLoggerAdvisor(), // 添加 spring AI 内置的日志记录功能
//                        new ZhipuLoggerAdvisor()
                        MessageChatMemoryAdvisor.builder(chatMemory).build()) // 添加自定义日志记录打印 Advisor
                .build();
    }
}
