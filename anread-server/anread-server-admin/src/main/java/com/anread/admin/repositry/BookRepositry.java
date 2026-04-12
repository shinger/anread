package com.anread.admin.repositry;

import com.anread.common.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepositry extends MongoRepository<Book, String>, PagingAndSortingRepository<Book, String>, CrudRepository<Book, String> {
    @Query("{'fileId': ?0}")
    Book findByFileId(String fileId);

    Page<Book> findByIsPrivateFalse(Pageable pageable);
}
