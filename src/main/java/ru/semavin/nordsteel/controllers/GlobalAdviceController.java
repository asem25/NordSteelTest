package ru.semavin.nordsteel.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.semavin.nordsteel.dtos.ResponseException;
import ru.semavin.nordsteel.util.exceptions.NoteNotFoundException;
import ru.semavin.nordsteel.util.exceptions.NoteWithTitleExistException;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для обработки ошибок в приложении.
 */
@ControllerAdvice
public class GlobalAdviceController {

    /**
     * Обрабатывает исключение, когда заметка не найдена.
     *
     * @param ex исключение NoteNotFoundException
     * @return ResponseEntity с кодом ошибки 404 и сообщением
     */
    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ResponseException> noteNotFound(NoteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseException.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .build());
    }

    /**
     * Обрабатывает ошибки валидации при передаче некорректных данных.
     *
     * @param ex исключение MethodArgumentNotValidException
     * @return ResponseEntity с кодом ошибки 400 и детальным описанием ошибок
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Обрабатывает ошибки валидации при аннотациях валидации (например, @NotNull).
     *
     * @param ex исключение ConstraintViolationException
     * @return ResponseEntity с кодом ошибки 400 и сообщением об ошибке
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseException> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseException.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build());
    }
    /**
     * Обрабатывает исключение, когда заметка найдена.
     *
     * @param ex исключение NoteWithTitleExistException
     * @return ResponseEntity с кодом ошибки 404 и сообщением
     */
    @ExceptionHandler(NoteWithTitleExistException.class)
    public ResponseEntity<ResponseException> handleNoteWithTitleExist(NoteWithTitleExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseException.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build());
    }
}
