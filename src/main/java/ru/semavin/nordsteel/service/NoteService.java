package ru.semavin.nordsteel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.semavin.nordsteel.dtos.NoteDTO;
import ru.semavin.nordsteel.mappers.NoteMapper;
import ru.semavin.nordsteel.models.Note;
import ru.semavin.nordsteel.repositories.NoteRepository;
import ru.semavin.nordsteel.util.ExceptionFactory;

import java.util.List;

/**
 * Сервис для управления заметками (Note).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    /**
     * Создаёт новую заметку.
     *
     * @param note Заметка для сохранения
     * @return Сохранённая заметка
     */
    @Transactional
    public NoteDTO createNote(NoteDTO note) {
        log.info("Создание новой заметки: {}", note);
        Note noteFromDTO = noteMapper.toEntity(note);
        if (noteRepository.existsByTitleIgnoreCase(noteFromDTO.getTitle())){
            return noteMapper.toDto(noteFromDTO);
        }
        noteRepository.save(noteFromDTO);
        return noteMapper.toDto(noteFromDTO);
    }

    /**
     * Возвращает все заметки из БД.
     *
     * @return Список заметок
     */
    public List<NoteDTO> getAllNotes() {
        log.info("Получение всех заметок из БД");
        return noteMapper.toDtoList(noteRepository.findAll());
    }

    /**
     * Возвращает заметку по её идентификатору.
     *
     * @param id Идентификатор заметки
     * @return Заметка, если найдена
     */
    public NoteDTO getNoteById(Long id) {
        log.info("Получение заметки по id: {}", id);
        Note note = noteRepository.findById(Math.toIntExact(id)).orElseThrow(() -> ExceptionFactory.noteNotFound(id));
        return noteMapper.toDto(note);
    }

    /**
     * Обновляет данные заметки.
     *
     * @param id          Идентификатор заметки
     * @param updatedNote Данные для обновления
     * @return Обновлённая заметка, если найдено
     */
    @Transactional
    public NoteDTO updateNote(Long id, NoteDTO updatedNote) {
        log.info("Обновление заметки с id: {}, новые данные: {}", id, updatedNote);
        if (noteRepository.existsByTitleIgnoreCase(updatedNote.getTitle())){
            throw ExceptionFactory.noteWithTitleExist(updatedNote.getTitle());
        }
        Note noteSaved = noteRepository.findById(Math.toIntExact(id)).map(note -> {
            note.setTitle(updatedNote.getTitle());
            note.setContent(updatedNote.getContent());
            return noteRepository.save(note);
        }).orElseThrow(() -> ExceptionFactory.noteNotFound(id));
        return noteMapper.toDto(noteSaved);
    }

    /**
     * Удаляет заметку по её идентификатору.
     *
     * @param id Идентификатор заметки
     */
    @Transactional
    public void deleteNote(Long id) {
        log.info("Удаление заметки с id: {}", id);
        if (!noteRepository.existsById(Math.toIntExact(id))) {
            log.warn("Заметка с ID {} не найдена и не может быть удалена", id);
            throw ExceptionFactory.noteNotFound(id);
        }
        noteRepository.deleteById(Math.toIntExact(id));
    }
}