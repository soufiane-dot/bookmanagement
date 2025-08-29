package com.api.bookmanagement.mapper;

import com.api.bookmanagement.domain.Author;
import com.api.bookmanagement.dto.AuthorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthorMapper {

    AuthorDTO toAuthorDTO(Author author);

    @Mapping(target = "id", ignore = true)
    Author toAuthor(AuthorDTO authorDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Author author, AuthorDTO authorDTO);
}
