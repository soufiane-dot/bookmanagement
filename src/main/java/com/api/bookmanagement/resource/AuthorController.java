package com.api.bookmanagement.resource;

import com.api.bookmanagement.dto.AuthorDTO;
import com.api.bookmanagement.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "All Authors", description = "Load All Authors")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorDTO.class))))})
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        log.info("Start resource: find all authors");
        var output = ResponseEntity.ok(authorService.getAllAuthors());
        log.info("End resource: find all authors");
        return output;
    }

    @Operation(summary = "Find author by ID", description = "Returns a single author")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation",
            content = @Content(schema = @Schema(implementation = AuthorDTO.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        log.info("Start resource: find author by ID: {}", id);
        var output = ResponseEntity.ok(authorService.getAuthorById(id));
        log.info("End resource: find author by ID: {}", id);
        return output;
    }

    @Operation(summary = "Create a new author", description = "Creates a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully",
                    content = @Content(schema = @Schema(implementation = AuthorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO authorDTO) {
        log.info("Start resource: create author: {}", authorDTO);
        var output = new ResponseEntity<>(authorService.createAuthor(authorDTO), HttpStatus.CREATED);
        log.info("End resource: create author: {}", authorDTO);
        return output;
    }

    @Operation(summary = "Update an existing author", description = "Updates an existing author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = @Content(schema = @Schema(implementation = AuthorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        log.info("Start resource: update author with ID: {}", id);
        var output = ResponseEntity.ok(authorService.updateAuthor(id, authorDTO));
        log.info("End resource: update author with ID: {}", id);
        return output;
    }

    @Operation(summary = "Delete an author", description = "Deletes an author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.info("Start resource: delete author with ID: {}", id);
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
