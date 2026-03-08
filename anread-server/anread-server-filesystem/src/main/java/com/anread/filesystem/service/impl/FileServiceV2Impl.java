package com.anread.filesystem.service.impl;

import com.anread.common.dto.Result;
import com.anread.common.enums.StateEnum;
import com.anread.common.exception.BizException;
import com.anread.common.utils.MD5Uitl;
import com.anread.common.vo.FileVo;
import com.anread.filesystem.service.FileServiceV2;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FileServiceV2Impl implements FileServiceV2 {
    @Override
    public Mono<Result<FileVo>> uploadBook(Mono<FilePart> filePartMono) {
        return filePartMono.flatMap(filePart ->
                // 1. 将 FilePart 的内容（Flux<DataBuffer>）聚合为一个单一的 DataBuffer
                DataBufferUtils.join(filePart.content())
                        .map(dataBuffer -> {
                            byte[] bytes;
                            try {
                                // 2. 分配与 DataBuffer 可读字节数相等的 byte[]
                                bytes = new byte[dataBuffer.readableByteCount()];
                                // 3. 将 DataBuffer 中的数据读取到 byte[] 中
                                dataBuffer.read(bytes);
                            } finally {
                                // 4. 关键：释放 DataBuffer 资源，防止内存泄漏
                                DataBufferUtils.release(dataBuffer);
                            }

                            // 5. 执行你的业务逻辑
                            String md5 = MD5Uitl.generateMD5(bytes);
                            FileVo fileVo = FileVo.builder()
                                    .id(md5)
                                    .coverImg("http://127.0.0.1:9000/books/" + md5 + "/cover.jpeg")
                                    .build();

                            return Result.<FileVo>success().data(fileVo);
                        })
                        // 6. 统一处理异常
                        .onErrorMap(e -> new BizException(StateEnum.REQUEST_DATA_ERROR))
        );
    }


}
