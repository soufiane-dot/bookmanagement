package com.api.bookmanagement.util;

import com.api.bookmanagement.domain.Author;
import com.api.bookmanagement.domain.Book;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class BookRatingCalculator {

    // Calculate book rating based on publication date and author
    public double calculateRating(Book book) {
        double publicationDateScore = calculatePublicationDateScore(book.getPublicationDate());
        double authorScore = calculateAuthorScore(book.getAuthor());

        return (publicationDateScore * 0.6) + (authorScore * 0.4);
    }

    private double calculatePublicationDateScore(LocalDate publicationDate) {
        // More recent books get higher scores
        long yearsSincePublication = ChronoUnit.YEARS.between(publicationDate, LocalDate.now());

        if (yearsSincePublication <= 5) {
            return 10 - (yearsSincePublication * 0.6);
        } else if (yearsSincePublication <= 15) {
            return 7 - ((yearsSincePublication - 5) * 0.3);
        } else {
            return Math.max(1, 4 - ((yearsSincePublication - 15) * 0.1));
        }
    }

    private double calculateAuthorScore(Author author) {
        int followers = author.getFollowersNumber();

        if (followers > 1000) {
            return 10.0;
        } else if (followers > 500) {
            return 8.0;
        } else if (followers > 100) {
            return 6.0;
        } else if (followers > 50) {
            return 4.0;
        } else {
            return 2.0;
        }
    }
}
