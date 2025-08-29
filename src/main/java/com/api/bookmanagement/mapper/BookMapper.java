package com.api.bookmanagement.mapper;

import com.api.bookmanagement.domain.Book;
import com.api.bookmanagement.dto.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    BookDTO toBookDTO(Book book);

    @Mapping(target = "id", ignore = true)
    Book toBookEntity(BookDTO bookDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Book book, BookDTO bookDTO);
}
