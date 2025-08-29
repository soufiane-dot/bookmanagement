package com.api.bookmanagement.service;

import com.api.bookmanagement.config.Messages;
import com.api.bookmanagement.domain.Author;
import com.api.bookmanagement.domain.Book;
import com.api.bookmanagement.dto.BookDTO;
import com.api.bookmanagement.exception.FunctionalException;
import com.api.bookmanagement.mapper.BookMapper;
import com.api.bookmanagement.repository.AuthorRepository;
import com.api.bookmanagement.repository.BookRepository;
import com.api.bookmanagement.util.GlobalConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private Messages messages;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDTO bookDTO;
    private Author author;
    private final Long bookId = 1L;
    private final Long authorId = 1L;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(authorId);
        author.setName("Stephen King");

        book = new Book();
        book.setId(bookId);
        book.setTitle("The Shining");
        book.setAuthor(author);

        bookDTO = new BookDTO();
        bookDTO.setId(bookId);
        bookDTO.setTitle("The Shining");
        bookDTO.setAuthorId(authorId);
        bookDTO.setAuthorName(author.getName());
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        // Arrange
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toBookDTO(any(Book.class))).thenReturn(bookDTO);

        // Act
        List<BookDTO> result = bookService.getAllBooks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDTO, result.get(0));
        verify(bookRepository).findAll();
        verify(bookMapper).toBookDTO(any(Book.class));
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        // Arrange
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        // Act
        BookDTO result = bookService.getBookById(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(bookDTO, result);
        verify(bookRepository).findById(bookId);
        verify(bookMapper).toBookDTO(book);
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldThrowException() {
        // Arrange
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        when(messages.get(eq(GlobalConstants.ERROR_BOOK_NOT_FOUND_ID), anyLong()))
                .thenReturn("Book not found with id: " + bookId);

        // Act & Assert
        FunctionalException exception = assertThrows(FunctionalException.class, () -> {
            bookService.getBookById(bookId);
        });

        assertEquals("Book not found with id: " + bookId, exception.getMessage());
        verify(bookRepository).findById(bookId);
        verify(messages).get(eq(GlobalConstants.ERROR_BOOK_NOT_FOUND_ID), eq(bookId));
    }

    @Test
    void getBookByTitle_WhenBookDoesNotExist_ShouldThrowException() {
        // Arrange
        String title = "Nonexistent Book";
        when(messages.get(eq(GlobalConstants.ERROR_BOOK_NOT_FOUND_TITLE), anyString()))
                .thenReturn("Book not found with title: " + title);

        FunctionalException exception = assertThrows(FunctionalException.class, () -> {
            bookService.getBookByTitle(title);
        });

        assertEquals("Book not found with title: " + title, exception.getMessage());
        verify(bookRepository).findByTitle(title);
        verify(messages).get(eq(GlobalConstants.ERROR_BOOK_NOT_FOUND_TITLE), eq(title));
    }

    @Test
    void createBook_WhenAuthorDoesNotExist_ShouldThrowException() {
        // Arrange
        when(authorRepository.existsById(authorId)).thenReturn(false);
        when(messages.get(eq(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID), anyLong()))
                .thenReturn("Author not found with id: " + authorId);

        // Act & Assert
        FunctionalException exception = assertThrows(FunctionalException.class, () -> {
            bookService.createBook(bookDTO);
        });

        assertEquals("Author not found with id: " + authorId, exception.getMessage());
        verify(authorRepository).existsById(authorId);
        verify(messages).get(eq(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID), eq(authorId));
    }

    @Test
    void updateBook_WhenBookDoesNotExist_ShouldThrowException() {
        // Arrange
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        when(messages.get(eq(GlobalConstants.ERROR_BOOK_NOT_FOUND_ID), anyLong()))
                .thenReturn("Book not found with id: " + bookId);

        // Act & Assert
        FunctionalException exception = assertThrows(FunctionalException.class, () -> {
            bookService.updateBook(bookId, bookDTO);
        });

        assertEquals("Book not found with id: " + bookId, exception.getMessage());
        verify(bookRepository).findById(bookId);
        verify(messages).get(eq(GlobalConstants.ERROR_BOOK_NOT_FOUND_ID), eq(bookId));
    }

    @Test
    void updateBook_WhenAuthorDoesNotExist_ShouldThrowException() {
        // Arrange
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.existsById(authorId)).thenReturn(false);
        when(messages.get(eq(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID), anyLong()))
                .thenReturn("Author not found with id: " + authorId);

        // Act & Assert
        FunctionalException exception = assertThrows(FunctionalException.class, () -> {
            bookService.updateBook(bookId, bookDTO);
        });

        assertEquals("Author not found with id: " + authorId, exception.getMessage());
        verify(bookRepository).findById(bookId);
        verify(authorRepository).existsById(authorId);
        verify(messages).get(eq(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID), eq(authorId));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldDeleteBook() {
        // Arrange
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // Act
        bookService.deleteBook(bookId);

        // Assert
        verify(bookRepository).existsById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void deleteBook_WhenBookDoesNotExist_ShouldThrowException() {
        // Arrange
        when(bookRepository.existsById(bookId)).thenReturn(false);
        when(messages.get(eq(GlobalConstants.ERROR_BOOK_NOT_FOUND_ID), anyLong()))
                .thenReturn("Book not found with id: " + bookId);

        // Act & Assert
        FunctionalException exception = assertThrows(FunctionalException.class, () -> {
            bookService.deleteBook(bookId);
        });

        assertEquals("Book not found with id: " + bookId, exception.getMessage());
        verify(bookRepository).existsById(bookId);
        verify(messages).get(eq(GlobalConstants.ERROR_BOOK_NOT_FOUND_ID), eq(bookId));
    }
}
