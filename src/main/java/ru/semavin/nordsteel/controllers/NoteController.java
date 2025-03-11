package ru.semavin.nordsteel.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.semavin.nordsteel.dtos.NoteDTO;
import ru.semavin.nordsteel.models.Note;
import ru.semavin.nordsteel.service.NoteService;

import java.util.List;


/**
 * Контроллер для управления заметками.
 */
@Tag(name = "Note Controller", description = "Управление заметками")
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;

    /**
     * Создаёт новую заметку.
     *
     * @param note Заметка для сохранения
     * @return Созданная заметка
     */
    @Operation(summary = "Создание новой заметки")
    @PostMapping
    public NoteDTO createNote(@RequestBody @Valid NoteDTO note) {
        log.info("Запрос на создание заметки: {}", note);
        NoteDTO created = noteService.createNote(note);
        log.info("Заметка создана: {}", created);
        return created;
    }

    /**
     * Возвращает все заметки.
     *
     * @return Список заметок
     */
    @Operation(summary = "Получение всех заметок")
    @GetMapping
    public List<NoteDTO> getAllNotes() {
        log.info("Запрос на получение всех заметок");
        List<NoteDTO> notes = noteService.getAllNotes();
        log.info("Количество найденных заметок: {}", notes.size());
        return notes;
    }

    /**
     * Возвращает заметку по её ID.
     *
     * @param id Идентификатор заметки
     * @return Найденная заметка
     */
    @Operation(summary = "Получение заметки по ID")
    @GetMapping("/{id}")
    public NoteDTO getNoteById(@PathVariable Long id) {
        log.info("Запрос на получение заметки по ID: {}", id);
        NoteDTO note = noteService.getNoteById(id);
        log.info("Найдена заметка: {}", note);
        return note;
    }

    /**
     * Обновляет существующую заметку.
     *
     * @param id   Идентификатор заметки
     * @param note Новые данные заметки
     * @return Обновлённая заметка
     */
    @Operation(summary = "Обновление заметки")
    @PutMapping("/{id}")
    public NoteDTO updateNote(@PathVariable Long id, @RequestBody @Valid NoteDTO note) {
        log.info("Запрос на обновление заметки ID: {}, новые данные: {}", id, note);
        NoteDTO updated = noteService.updateNote(id, note);
        log.info("Заметка обновлена: {}", updated);
        return updated;
    }

    /**
     * Удаляет заметку по её ID.
     *
     * @param id Идентификатор заметки
     */
    @Operation(summary = "Удаление заметки")
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        log.info("Запрос на удаление заметки с ID: {}", id);
        noteService.deleteNote(id);
        log.info("Заметка с ID {} успешно удалена", id);
    }
}