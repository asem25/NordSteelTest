package ru.semavin.nordsteel.util.exceptions;

public class NoteWithTitleExistException extends RuntimeException {
    public NoteWithTitleExistException(String message) {
        super(message);
    }
}
