package ru.semavin.nordsteel.util;

import lombok.extern.slf4j.Slf4j;
import ru.semavin.nordsteel.util.exceptions.NoteNotFoundException;
import ru.semavin.nordsteel.util.exceptions.NoteWithTitleExistException;

/**
 * Фабрика для создания исключений, связанных с Note.
 * Упрощает единообразное формирование исключений в приложении.
 */
@Slf4j
public final class ExceptionFactory {

    private ExceptionFactory() {
    }

    /**
     * Создаёт исключение NoteNotFoundException.
     *
     * @param id Идентификатор заметки
     * @return NoteNotFoundException
     */
    public static NoteNotFoundException noteNotFound(Long id) {
        String msg = String.format("Заметка с id = %d не найдена", id);
        log.error(msg);
        return new NoteNotFoundException(msg);
    }
    /**
     * Создаёт исключение NoteWithTitleExistException.
     *
     * @param title Заголовок заметки
     * @return NoteWithTitleExistException
     */
    public static NoteWithTitleExistException noteWithTitleExist(String title) {
        String msg = String.format("Заметка с title = %s найдена, уже существует", title);
        log.error(msg);
        return new NoteWithTitleExistException(msg);
    }
}