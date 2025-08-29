package com.api.bookmanagement.service;

import com.api.bookmanagement.config.Messages;
import com.api.bookmanagement.domain.Author;
import com.api.bookmanagement.dto.AuthorDTO;
import com.api.bookmanagement.exception.FunctionalException;
import com.api.bookmanagement.mapper.AuthorMapper;
import com.api.bookmanagement.repository.AuthorRepository;
import com.api.bookmanagement.util.GlobalConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private Messages messages;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorDTO authorDTO;
    private final Long authorId = 1L;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(authorId);
        author.setName("Stephen King");

        authorDTO = new AuthorDTO();
        authorDTO.setId(authorId);
        authorDTO.setName("Stephen King");
    }

    @Test
    void getAllAuthors_ShouldReturnAllAuthors() {
        List<Author> authors = Collections.singletonList(author);
        when(authorRepository.findAll()).thenReturn(authors);
        when(authorMapper.toAuthorDTO(any(Author.class))).thenReturn(authorDTO);

        List<AuthorDTO> result = authorService.getAllAuthors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(authorDTO, result.get(0));
        verify(authorRepository).findAll();
        verify(authorMapper).toAuthorDTO(any(Author.class));
    }

    @Test
    void getAuthorById_WhenAuthorExists_ShouldReturnAuthor() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.getAuthorById(authorId);

        assertNotNull(result);
        assertEquals(authorDTO, result);
        verify(authorRepository).findById(authorId);
        verify(authorMapper).toAuthorDTO(author);
    }

    @Test
    void getAuthorById_WhenAuthorDoesNotExist_ShouldThrowException() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());
        when(messages.get(eq(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID), anyLong()))
                .thenReturn("Author not found with id: " + authorId);

        FunctionalException exception = assertThrows(FunctionalException.class, () -> {
            authorService.getAuthorById(authorId);
        });

        assertEquals("Author not found with id: " + authorId, exception.getMessage());
        verify(authorRepository).findById(authorId);
        verify(messages).get(eq(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID), eq(authorId));
    }

    @Test
    void createAuthor_ShouldSaveAndReturnAuthor() {
        when(authorMapper.toAuthor(authorDTO)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.createAuthor(authorDTO);

        assertNotNull(result);
        assertEquals(authorDTO, result);
        verify(authorMapper).toAuthor(authorDTO);
        verify(authorRepository).save(author);
        verify(authorMapper).toAuthorDTO(author);
    }

    @Test
    void updateAuthor_WhenAuthorExists_ShouldUpdateAndReturnAuthor() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.updateAuthor(authorId, authorDTO);

        assertNotNull(result);
        assertEquals(authorDTO, result);
        verify(authorRepository).findById(authorId);
        verify(authorMapper).updateEntity(author, authorDTO);
        verify(authorRepository).save(author);
        verify(authorMapper).toAuthorDTO(author);
    }

    @Test
    void updateAuthor_WhenAuthorDoesNotExist_ShouldThrowException() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());
        when(messages.get(eq(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID), anyLong()))
                .thenReturn("Author not found with id: " + authorId);

        FunctionalException exception = assertThrows(FunctionalException.class, () -> {
            authorService.updateAuthor(authorId, authorDTO);
        });

        assertEquals("Author not found with id: " + authorId, exception.getMessage());
        verify(authorRepository).findById(authorId);
        verify(messages).get(eq(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID), eq(authorId));
    }

    @Test
    void deleteAuthor_WhenAuthorExists_ShouldDeleteAuthor() {
        when(authorRepository.existsById(authorId)).thenReturn(true);

        authorService.deleteAuthor(authorId);

        verify(authorRepository).existsById(authorId);
        verify(authorRepository).deleteById(authorId);
    }

    @Test
    void deleteAuthor_WhenAuthorDoesNotExist_ShouldThrowException() {
        when(authorRepository.existsById(authorId)).thenReturn(false);
        when(messages.get(eq(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID), anyLong()))
                .thenReturn("Author not found with id: " + authorId);

        FunctionalException exception = assertThrows(FunctionalException.class, () -> {
            authorService.deleteAuthor(authorId);
        });

        assertEquals("Author not found with id: " + authorId, exception.getMessage());
        verify(authorRepository).existsById(authorId);
        verify(messages).get(eq(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID), eq(authorId));
    }
}