package ru.semavin.nordsteel.util.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Исключение, генерируемое при отсутствии заметки.
 */
@Schema(description = "Исключение, генерируемое при отсутствии заметки.")
public class NoteNotFoundException extends RuntimeException {

    /**
     * Конструктор с сообщением.
     *
     * @param message Сообщение об ошибке
     */
    public NoteNotFoundException(String message) {
        super(message);
    }
}