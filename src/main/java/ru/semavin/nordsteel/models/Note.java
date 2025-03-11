package ru.semavin.nordsteel.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;


/**
 * Модель заметки (Note).
 */
@Schema(description = "Заметка, содержащая заголовок и контент")
@Entity
@Table(name = "note")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    /**
     * Уникальный идентификатор заметки.
     */
    @Schema(description = "Уникальный идентификатор заметки", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Заголовок заметки.
     */
    @Schema(description = "Заголовок заметки", example = "Мои планы")
    private String title;

    /**
     * Содержимое заметки.
     */
    @Schema(description = "Текст заметки", example = "Сегодня я купил...")
    private String content;
}
