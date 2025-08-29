package com.api.bookmanagement.repository;

import com.api.bookmanagement.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    
    @Query("SELECT DISTINCT b.author FROM Book b WHERE b.id IN :bookIds")
    List<com.api.bookmanagement.domain.Author> findAuthorsByBookIds(List<Long> bookIds);
}