package com.api.bookmanagement.service;

import com.api.bookmanagement.config.Messages;
import com.api.bookmanagement.domain.Author;
import com.api.bookmanagement.dto.AuthorDTO;
import com.api.bookmanagement.exception.FunctionalException;
import com.api.bookmanagement.mapper.AuthorMapper;
import com.api.bookmanagement.repository.AuthorRepository;
import com.api.bookmanagement.util.GlobalConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final Messages messages;

    public List<AuthorDTO> getAllAuthors() {
        log.info("Start service: Getting all authors");
        return authorRepository.findAll().stream()
                .map(authorMapper::toAuthorDTO)
                .toList();
    }

    public AuthorDTO getAuthorById(Long id) {
        log.info("Start service: Getting author by id: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(messages.get(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID, id)));
        var output = authorMapper.toAuthorDTO(author);
        log.info("End service: Getting author by id: {}", id);
        return output;
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        log.info("Start service: Creating author: {}", authorDTO);
        Author author = authorMapper.toAuthor(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        var output = authorMapper.toAuthorDTO(savedAuthor);
        log.info("End service: Created author: {}", authorDTO);
        return output;
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        log.info("Start service: Updating author with id: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(messages.get(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID, id)));

        authorMapper.updateEntity(author, authorDTO);
        Author updatedAuthor = authorRepository.save(author);
        var output = authorMapper.toAuthorDTO(updatedAuthor);
        log.info("End service: Updated author with id: {}", id);
        return output;
    }

    public void deleteAuthor(Long id) {
        log.info("Start service: Deleting author with id: {}", id);
        if (!authorRepository.existsById(id)) {
            throw new FunctionalException(messages.get(GlobalConstants.ERROR_AUTHOR_NOT_FOUND_ID, id));
        }
        authorRepository.deleteById(id);
        log.info("End service: Deleted author with id: {}", id);
    }
}