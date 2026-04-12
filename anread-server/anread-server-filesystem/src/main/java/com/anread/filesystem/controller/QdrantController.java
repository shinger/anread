package com.anread.filesystem.controller;

import com.anread.common.dto.QdrantBookQuery;
import com.anread.common.dto.Result;
import com.anread.filesystem.service.QdrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/qdrant")
public class QdrantController {

    @Autowired
    private QdrantService qdrantService;

    @PostMapping("/search")
    public Result searchQuery(@RequestBody QdrantBookQuery query) {
        List<String> search = qdrantService.search(query.getQuery(), query.getBookId(), query.getMaxResults());
        return Result.success().data(search);
    }
}
