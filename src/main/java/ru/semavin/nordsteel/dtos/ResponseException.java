package ru.semavin.nordsteel.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO-класс для передачи информации об ошибках.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseException {
    /**
     * HTTP-статус ошибки.
     */
    private int status;

    /**
     * Сообщение об ошибке.
     */
    private String message;
}
