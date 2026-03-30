package com.anread.book.repository;

import com.anread.common.entity.Book;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    /**
     * 根据分类ID列表查询书本
     *
     * @param categoryIds 分类ID列表
     * @return 书本列表
     */
    @Query("{categoryId: {$in: ?0}, status: 1}")
    Flux<Book> findByCategoryIds(List<Integer> categoryIds);

     /**
     * 根据ID列表查询书本
     *
     * @param bookIds 书本ID列表
     * @return 书本列表
     */
    @Aggregation(pipeline = {
            "{ $match: { _id: { $in: ?0 }, status: 1 } }",  // 阶段1：筛选符合条件的文档
            "{ $addFields: { sortIndex: { $indexOfArray: [?0, { $toString: '$_id' }] } } }",  // 阶段2：计算每个文档在bookIds中的索引
            "{ $sort: { sortIndex: 1 } }",  // 阶段3：按索引升序排序（保证和bookIds顺序一致）
//            "{ $project: { sortIndex: 0 } }"  // 阶段4：隐藏临时的sortIndex字段（可选，不影响结果）
    })
    Flux<Book> findByIds(List<String> bookIds);

    /**
     * 随机查询书本
     *
     * @return 随机书本列表
     */
    @Aggregation(pipeline = {"{ $match: { status: 1, isPrivate: false } }","{ $sample: { size: 4 } }"})
    Flux<Book> findByRandom();

    /**
     * 搜索书本
     * @param keyword 搜索关键词
     * @return 搜索到的书本列表
     */
    @Query("{$or: [{title: {$regex: ?0, $options: 'i'}}, {author: {$regex: ?0, $options: 'i'}}], status: 1}")
    Flux<Book> findByTitleOrAuthorContaining(String keyword);

}
