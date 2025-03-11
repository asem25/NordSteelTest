package ru.semavin.nordsteel.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.semavin.nordsteel.service.NoteService;
import org.mockito.Mockito;

@TestConfiguration
public class TestConfig {

    @Bean
    public NoteService noteService() {
        return Mockito.mock(NoteService.class);
    }
}