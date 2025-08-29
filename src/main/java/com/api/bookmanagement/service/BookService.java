package com.api.bookmanagement.service;

import com.api.bookmanagement.config.Messages;
import com.api.bookmanagement.domain.Author;
import com.api.bookmanagement.domain.Book;
import com.api.bookmanagement.dto.AuthorDTO;
import com.api.bookmanagement.dto.BookDTO;
import com.api.bookmanagement.exception.FunctionalException;
import com.api.bookmanagement.mapper.AuthorMapper;
import com.api.bookmanagement.mapper.BookMapper;
import com.api.bookmanagement.util.GlobalConstants;
import com.api.bookmanagement.repository.AuthorRepository;
import com.api.bookmanagement.repository.BookRepository;
import com.api.bookmanagement.util.BookRatingCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final BookMapper bookMapper;

    private final AuthorMapper authorMapper;

    private final BookRatingCalculator bookRatingCalculator;

    private final Messages messages;

    private final RestTemplate restTemplate;

    @Value("${openlibrary.api.url}")
    private String openLibraryApiUrl;

    public List<BookDTO> getAllBooks() {
        log.info("Start service: Getting all books");
        var books = bookRepository.findAll().stream().map(bookMapper::toBookDTO).toList();
        log.info("End service: Getting all books");
        return books;
    }

    public BookDTO getBookById(Long id) {
        log.info("Start service: Getting book by id: {} ", id);
        Book book = bookRepository.findById(id).orElseThrow(() -> 
            new FunctionalException(messages.get(GlobalConstants.ERROR_BOOK_NOT_FOUND_ID, id)));
        var output = bookMapper.toBookDTO(book);
        log.info("End service: Getting book by id: {}", id);
        return output;
    }

    public BookDTO getBookByTitle(String title) {
        log.info("Start service: Getting book by title: {} ", title);
        Book book = bookRepository.findByTitle(title).orElseThrow(() -> 
            new FunctionalException(messages.get(GlobalConstants.ERROR_BOOK_NOT_FOUND_TITLE, title)));
        var output = bookMapper.toBookDTO(book);
        log.info("End service: Getting book by title: {}", title);
        return output;
    }

    public BookDTO createBook(BookDTO bookDTO) {
        log.info("Start service: Creating book with data: {}", bookDTO);
        if (!authorRepository.existsById(bookDTO.getAuthorId())) {
            throw new FunctionalException(messages.get(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID, bookDTO.getAuthorId()));
        }
        var savedBook = bookRepository.save(bookMapper.toBookEntity(bookDTO));
        var output = bookMapper.toBookDTO(savedBook);
        log.info("End service: Creating book with data: {}", bookDTO);
        return output;
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        log.info("Start service: Updating book with id: {} ", id);
        Book book = bookRepository.findById(id).orElseThrow(() -> 
            new FunctionalException(messages.get(GlobalConstants.ERROR_BOOK_NOT_FOUND_ID, id)));

        if (!authorRepository.existsById(bookDTO.getAuthorId())) {
            throw new FunctionalException(messages.get(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID, bookDTO.getAuthorId()));
        }

        bookMapper.updateEntity(book, bookDTO);

        Book updatedBook = bookRepository.save(book);
        var output = bookMapper.toBookDTO(updatedBook);
        log.info("End service: Updating book with id: {} ", id);
        return output;
    }

    public void deleteBook(Long id) {
        log.info("Start service: Deleting book with id: {} ", id);
        if (!bookRepository.existsById(id)) {
            throw new FunctionalException(messages.get(GlobalConstants.ERROR_BOOK_NOT_FOUND_ID, id));
        }
        bookRepository.deleteById(id);
        log.info("End service: Deleting book with id: {} ", id);
    }

    public double getRatingForBook(Long id) {
        log.info("Start service: Getting rating for id: {} ", id);
        Book book = bookRepository.findById(id).orElseThrow(() -> 
            new FunctionalException(messages.get(GlobalConstants.ERROR_BOOK_NOT_FOUND_ID, id)));
        var output = bookRatingCalculator.calculateRating(book);
        log.info("End service: Getting rating for id: {} ", id);
        return output;
    }

    public List<AuthorDTO> getAuthorsByBookIds(List<Long> bookIds) {
        log.info("Start service: Getting authors by ids: {}", bookIds);
        List<Author> authors = bookRepository.findAuthorsByBookIds(bookIds);
        var authorsWithBooks = authors.stream().map(authorMapper::toAuthorDTO).toList();
        log.info("End service: Getting authors by ids: {}", bookIds);
        return authorsWithBooks;
    }

    public Map<String, Object> findBookByIsbn(String isbn) {
        String url = openLibraryApiUrl + "?bibkeys=ISBN:" + isbn + "&format=json";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || response.isEmpty()) {
            throw new FunctionalException(messages.get(GlobalConstants.ERROR_BOOK_NOT_FOUND_ISBN, isbn));
        }

        return response;
    }
}
