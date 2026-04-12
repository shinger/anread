package com.anread.book.service.impl;

import com.anread.book.mapper.AiChatRecordMapper;
import com.anread.book.repository.BookRepository;
import com.anread.book.service.ChatService;
import com.anread.common.dto.AiChatRecordDto;
import com.anread.common.dto.ChatMessageDto;
import com.alibaba.fastjson2.JSON;
import com.anread.common.dto.Result;
import com.anread.common.entity.AiChatRecord;
import com.anread.common.entity.Book;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    private final WebClient webClient;
    private final String model;

    @Autowired
    private AiChatRecordMapper aiChatRecordMapper;

    @Autowired
    private BookRepository bookRepository;

    public ChatServiceImpl(WebClient deepSeekWebClient,
                           @Value("${ai.chat.model}") String model) {
        this.webClient = deepSeekWebClient;
        this.model = model;
    }

    @Override
    public Flux<String> chat(ChatMessageDto request) {
        List<Map<String, String>> messages = new ArrayList<>();
        // 查询图书信息填充提示词
        Book book = bookRepository.findById(request.getBookId()).block();
        messages.add(Map.of("role", "system", "content", "你是一个图书问答助手，你的任务是回答用户关于图书的问题。\n图书信息如下：\n书名：" + book.getTitle() + "\n作者：" + book.getAuthor() + "\n简介：" + book.getIntroduction() + "\n出版社：" + book.getPress() + "\n出版年份：" + book.getPressYear() + "\n出版月份：" + book.getPressMonth()));

        // 查询历史对话填充上下文
        List<AiChatRecord> aiChatRecords = aiChatRecordMapper.selectList(new LambdaQueryWrapper<AiChatRecord>()
                .eq(AiChatRecord::getUserId, request.getUserId())
                .eq(AiChatRecord::getBookId, request.getBookId())
                .orderByAsc(AiChatRecord::getCreateTime));
        for (AiChatRecord aiChatRecord : aiChatRecords) {
            String userContent = "引用：" + (aiChatRecord.getQuotation() != null ? aiChatRecord.getQuotation() : "无引用信息") + "\n提问：" + aiChatRecord.getQuestion();
            messages.add(Map.of("role", "user", "content", userContent));
            messages.add(Map.of("role", "assistant", "content", aiChatRecord.getAnswer()));
        }

        messages.add(Map.of("role", "user", "content", request.getMessage()));

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", messages,
                "stream", true
        );

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty("请求失败")
                                .map(err -> new RuntimeException("API 异常：" + response.statusCode() + " " + err))
                )
                // ✅ 直接返回原始流，网关不缓冲、不拦截
                .bodyToFlux(String.class);
    }

    @Override
    public Flux<String> chatRAG(ChatMessageDto request) {
        return null;
    }

    @Override
    public Result<Long> uploadRecord(AiChatRecordDto request) {
        AiChatRecord aiChatRecord = new AiChatRecord();
        BeanUtils.copyProperties(request, aiChatRecord);
        aiChatRecord.setCreateTime(LocalDateTime.now());
        aiChatRecordMapper.insert(aiChatRecord);
        return Result.<Long>success().data(aiChatRecord.getId());
    }

    @Override
    public Result<List<AiChatRecord>> getRecords(String userId, String bookId) {
        List<AiChatRecord> aiChatRecords = aiChatRecordMapper.selectList(new LambdaQueryWrapper<AiChatRecord>()
                .eq(AiChatRecord::getUserId, userId)
                .eq(AiChatRecord::getBookId, bookId));
        return Result.<List<AiChatRecord>>success().data(aiChatRecords);
    }

    @Override
    public Result updateRecord(AiChatRecord record) {
        aiChatRecordMapper.updateById(record);
        return Result.success();
    }

    @Override
    public Result deleteRecord(Long id) {
        aiChatRecordMapper.deleteById(id);
        return Result.success();
    }
}