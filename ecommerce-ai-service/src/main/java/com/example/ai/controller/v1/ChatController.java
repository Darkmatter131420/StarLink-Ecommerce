package com.example.ai.controller.v1;

import com.example.ai.domain.AiOrderQueryDto;
import com.example.ai.service.ChatService;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.common.exception.SystemException;
import com.example.common.util.UserContextUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
@Tag(name = "ai服务接口", description = "用于ai查询订单信息,自动下单,智能助手")
public class ChatController {

    private final ChatService chatService;

    @Resource
    private ChatClient chatClient;

    @Operation(summary = "ai查询订单信息")
    @PostMapping("/orders/query")
    public ResponseResult<List<OrderInfoVo>> processOrderQuery(@RequestBody @Validated AiOrderQueryDto userQuery) throws JsonProcessingException {
        UserContextUtil.getUserId();
        List<OrderInfoVo> orderInfoVos = chatService.processOrderQuery(userQuery);
        if (orderInfoVos != null) {
            return ResponseResult.success(orderInfoVos);
        }
        throw new SystemException("查询失败");
    }

    @Operation(summary = "ai自动下单")
    @PostMapping("/orders/auto-place")
    public ResponseResult<List<OrderInfoVo>> processOrderAuto(@RequestBody @Validated AiOrderQueryDto userQuery) throws JsonProcessingException {
        UserContextUtil.getUserId();
        List<OrderInfoVo> orderInfoVos = chatService.processOrderAuto(userQuery);
        if (orderInfoVos == null) {
            throw new SystemException("查询失败");
        }
        return ResponseResult.success(orderInfoVos);
    }

    @Operation(summary = "ai智能助手")
    @GetMapping("/chat/assistant")
    public ResponseResult<String> processChatAssistant(@RequestParam("userQuery") String userQuery)  {
        UserContextUtil.getUserId();
        String response = chatService.processChatAssistant(userQuery);
        if (response == null) {
            throw new SystemException("回复失败");
        }
        return ResponseResult.success(response);
    }

    @Operation(summary = "ai助手流式对话")
    @GetMapping(value = "/chat/stream-assistant", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChatAssistant(@RequestParam("userQuery") String userQuery,
                                            @RequestParam("chatId") String chatId)  {
        UserContextUtil.getUserId();

        // 系统角色消息
//        SystemMessage systemMessage = new SystemMessage("你是星链电商平台上的全能助手，你的名字叫星星，你需要根据用户的需求解决用户的疑惑或问题");
        // 用户角色消息
        UserMessage userMessage = new UserMessage(userQuery);
        // 构建提示词
//        Prompt prompt = new  Prompt(systemMessage, userMessage);

        return chatClient.prompt()
                .messages(userMessage)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();

    }
}