package ru.semavin.nordsteel.mappers;

import org.mapstruct.Mapper;
import ru.semavin.nordsteel.dtos.NoteDTO;
import ru.semavin.nordsteel.models.Note;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteDTO toDto(Note note);

    Note toEntity(NoteDTO noteDto);

    List<NoteDTO> toDtoList(List<Note> notes);

    List<Note> toEntityList(List<NoteDTO> dtos);
}
