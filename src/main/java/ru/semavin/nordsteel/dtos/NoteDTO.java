package ru.semavin.nordsteel.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи информации о заметке.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteDTO {
    /**
     * Уникальный идентификатор заметки.
     */
    private Long id;
    /**
     * Заголовок заметки.
     */
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 3, max = 100, message = "Заголовок должен содержать от 3 до 100 символов")
    private String title;

    /**
     * Описание заметки.
     */
    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 5, max = 500, message = "Описание должно содержать от 5 до 500 символов")
    private String content;
}
