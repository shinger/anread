package com.anread.book.controller;

import com.anread.book.service.ChatService;
import com.anread.book.service.LLMService;
import com.anread.common.dto.AiChatRecordDto;
import com.anread.common.dto.ChatMessageDto;
import com.anread.common.dto.Result;
import com.anread.common.entity.AiChatRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private LLMService llmService;

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestHeader("X-User-ID") String userId, @RequestBody ChatMessageDto request) {
        request.setUserId(userId);
        return chatService.chat(request);
    }

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/rag")
    public Flux<String> chatRAG(@RequestHeader("X-User-ID") String userId, @RequestBody ChatMessageDto request) {
        request.setUserId(userId);
        return llmService.answerQuestionStreaming(request.getUserId(), request.getBookId(), request.getMessage(), request.getQuotation());
    }

    @PostMapping("/upload")
    public Result<Long> uploadRecord(@RequestHeader("X-User-ID") String userId, @RequestBody AiChatRecordDto request) {
        request.setUserId(userId);
        return chatService.uploadRecord(request);
    }

    @GetMapping("/records")
    public Result<List<AiChatRecord>> getRecords(@RequestHeader("X-User-ID") String userId, @RequestParam("bookId") String bookId) {
        return chatService.getRecords(userId, bookId);
    }

    @PutMapping("/update")
    public Result updateRecord(@RequestBody AiChatRecord record) {
        return chatService.updateRecord(record);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteRecord(@PathVariable Long id) {
        return chatService.deleteRecord(id);
    }
}
