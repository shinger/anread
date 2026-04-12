package com.anread.filesystem.listener;

import com.anread.common.entity.BookFile;
import com.anread.common.entity.Chapter;
import com.anread.filesystem.config.RabbitMQConfig;
import com.anread.filesystem.mapper.BookFileMapper;
import com.anread.filesystem.service.QdrantService;
import com.anread.filesystem.utils.EpublibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Slf4j
@Component
public class MessageListener {

    @Autowired
    private BookFileMapper bookFileMapper;

    @Autowired
    private EpublibUtil epublibUtil;

    @Autowired
    private QdrantService qdrantService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_BOOK_SPLIT)
    public void receive(String bookId) {
        BookFile bookFile = bookFileMapper.selectOne(new LambdaQueryWrapper<BookFile>()
                .eq(BookFile::getBookId, bookId));

        try {
            if (bookFile == null) {
                log.error("接受到bookId不存在：{}", bookId);
                return;
            }

            if (bookFile.getIsVectorized() != null && bookFile.getIsVectorized()) {
                // 已经作过向量化
                log.warn("图书已向量化：{}", bookId);
                return;
            }

            URL url = new URL(bookFile.getFileUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Host", "127.0.0.1:9000"); // 必须和 URL 中的 host 一致
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Java EPUB Parser)");

            List<Chapter> chapters = epublibUtil.parse2Chapters(connection.getInputStream());
            qdrantService.storeChapters(bookId, bookFile.getTitle(), chapters);
            bookFile.setIsVectorized(true);
            bookFileMapper.updateById(bookFile);
        } catch (MalformedURLException e) {
            log.error("消息接受失败：URL错误：{}", bookFile.getFileUrl());
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("消息接受失败：打开响应流失败：{}", bookFile.getFileUrl());
            throw new RuntimeException(e);
        }

    }

}
