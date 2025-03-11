package ru.semavin.nordsteel.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.semavin.nordsteel.config.TestConfig;
import ru.semavin.nordsteel.dtos.NoteDTO;
import ru.semavin.nordsteel.service.NoteService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import org.mockito.ArgumentMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Тестовый класс для {@link NoteController}.
 * Проверяет корректность работы эндпоинтов управления заметками.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(NoteController.class)
@Import(TestConfig.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteService noteService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Тест на получение всех заметок.
     */
    @Test
    void testGetAllNotes() throws Exception {
        NoteDTO noteDTO = NoteDTO.builder().title("Test Title").content("Test Content").build();
        List<NoteDTO> notes = Collections.singletonList(noteDTO);

        when(noteService.getAllNotes()).thenReturn(notes);

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Title")))
                .andExpect(jsonPath("$[0].content", is("Test Content")));
    }

    /**
     * Тест на получение заметки по ID.
     */
    @Test
    void testGetNoteById() throws Exception {
        NoteDTO noteDTO = NoteDTO.builder().title("Test Title").content("Test Content").build();

        when(noteService.getNoteById(ArgumentMatchers.anyLong())).thenReturn(noteDTO);

        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Title")))
                .andExpect(jsonPath("$.content", is("Test Content")));
    }

    /**
     * Тест на создание новой заметки.
     */
    @Test
    void testCreateNote() throws Exception {
        NoteDTO noteDTO = NoteDTO.builder().title("Test Title").content("Test Content").build();

        when(noteService.createNote(ArgumentMatchers.any(NoteDTO.class))).thenReturn(noteDTO);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Title")))
                .andExpect(jsonPath("$.content", is("Test Content")));
    }

    /**
     * Тест на обновление заметки.
     */
    @Test
    void testUpdateNote() throws Exception {
        NoteDTO noteDTO = NoteDTO.builder().title("Updated Title").content("Updated Content").build();

        when(noteService.updateNote(ArgumentMatchers.anyLong(), ArgumentMatchers.any(NoteDTO.class))).thenReturn(noteDTO);

        mockMvc.perform(put("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.content", is("Updated Content")));
    }

    /**
     * Тест на удаление заметки по ID.
     */
    @Test
    void testDeleteNote() throws Exception {
        doNothing().when(noteService).deleteNote(ArgumentMatchers.anyLong());

        mockMvc.perform(delete("/api/notes/1"))
                .andExpect(status().isOk());
    }
}