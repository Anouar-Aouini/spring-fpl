package com.fivepoints.spring.repositories;

import com.fivepoints.spring.entities.Book;
import com.fivepoints.spring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContaining(String title);
    @Query("SELECT b FROM Book b WHERE b.id = :book_id ")
    Book findByIdTwo(@Param("book_id") Long book_id );
}
