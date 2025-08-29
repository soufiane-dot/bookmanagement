package com.api.bookmanagement.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalConstants {

    /**
     * API ERROR CODES
     */

    public static final String URI_TECHNICAL_EXCEPTION = "/problem/technical-exception";
    public static final String URI_FUNCTIONAL_EXCEPTION = "/problem/functional-exception";

    /**
     * codes messages
     */
    public static final String ERROR_WS_TECHNICAL = "error.ws.technical";

    /**
     * Book error messages
     */
    public static final String ERROR_BOOK_NOT_FOUND_ID = "error.book.not_found_id";
    public static final String ERROR_BOOK_NOT_FOUND_TITLE = "error.book.not_found_title";
    public static final String ERROR_BOOK_NOT_FOUND_ISBN = "error.book.not_found_isbn";
    
    /**
     * Author error messages
     */
    public static final String ERROR_AUTHOR_NOT_FOUND_ID = "error.author.not_found_id";

    /**
     * Security error messages
     */
    public static final String ERROR_INVALID_API_KEY = "error.invalid.api_key";
}
