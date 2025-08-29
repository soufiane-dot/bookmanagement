package com.api.bookmanagement.resource;

import com.api.bookmanagement.config.Messages;
import com.api.bookmanagement.dto.AuthorDTO;
import com.api.bookmanagement.dto.BookDTO;
import com.api.bookmanagement.service.BookService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;
    private final Messages messages;

    @Operation(summary = "Retrieve all books", description = "Fetch a list of all books in the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))))})
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        log.info("Start resource: Retrieve all books");
        var output = ResponseEntity.ok(bookService.getAllBooks());
        log.info("End resource: Retrieve all books");
        return output;
    }

    @Operation(summary = "Get book by ID", description = "Fetch a single book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        log.info("Start resource: Retrieve book by ID: {}", id);
        var output = ResponseEntity.ok(bookService.getBookById(id));
        log.info("End resource: Retrieve book by ID: {}", id);
        return output;
    }

    @Operation(summary = "Get book by title", description = "Fetch a single book by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @GetMapping("/title/{title}")
    public ResponseEntity<BookDTO> getBookByTitle(@PathVariable String title) {
        log.info("Start resource: Retrieve book by title: {}", title);
        var output = ResponseEntity.ok(bookService.getBookByTitle(title));
        log.info("End resource: Retrieve book by title: {}", title);
        return output;
    }

    @Operation(summary = "Create a new book", description = "Add a new book to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        log.info("Start resource: Create book: {}", bookDTO);
        var output = new ResponseEntity<>(bookService.createBook(bookDTO), HttpStatus.CREATED);
        log.info("End resource: Create book: {}", bookDTO);
        return output;
    }

    @Operation(summary = "Update an existing book", description = "Modify details of an existing book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        log.info("Start resource: Update book with id: {}", id);
        var output = ResponseEntity.ok(bookService.updateBook(id, bookDTO));
        log.info("End resource: Update book with id: {}", id);
        return output;
    }

    @Operation(summary = "Delete a book", description = "Remove a book from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("Start resource: Delete book with id: {}", id);
        bookService.deleteBook(id);
        log.info("End resource: Delete book with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get book ratings", description = "Fetch the average rating for a specific book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getBookRating(@PathVariable Long id) {
        log.info("Start resource: Get book rating: {}", id);
        var output = ResponseEntity.ok(bookService.getRatingForBook(id));
        log.info("End resource: Get book rating: {}", id);
        return output;
    }

    @Operation(summary = "Get authors by book IDs", description = "Fetch authors for a list of book IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/authors")
    public ResponseEntity<List<AuthorDTO>> getAuthorsByBookIds(@RequestBody List<Long> bookIds) {
        log.info("Start resource: Get authors by book IDs: {}", bookIds);
        var output = ResponseEntity.ok(bookService.getAuthorsByBookIds(bookIds));
        log.info("End resource: Get authors by book IDs: {}", bookIds);
        return output;
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Map<String, Object>> lookupBookByIsbn(@PathVariable String isbn) {
        log.info("Received request to lookup book with ISBN: {}", isbn);
        var output = ResponseEntity.ok(bookService.findBookByIsbn(isbn));
        log.info("Completed lookup for book with ISBN: {}", isbn);
        return output;
    }
}
