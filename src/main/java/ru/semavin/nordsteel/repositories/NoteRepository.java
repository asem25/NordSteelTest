package ru.semavin.nordsteel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.semavin.nordsteel.models.Note;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    boolean existsByTitleIgnoreCase(String title);

}
