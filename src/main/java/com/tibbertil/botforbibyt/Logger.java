package com.tibbertil.botforbibyt;

import com.tibbertil.botforbibyt.entity.Level;
import com.tibbertil.botforbibyt.entity.LogEventEntity;
import com.tibbertil.botforbibyt.repository.LogEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Logger {

    private final LogEventRepository logEventRepository;

    public void log(Level level, String message, String context) {
        LogEventEntity event = new LogEventEntity();
        event.setLevel(level);
        event.setMessage(message);
        event.setTimestamp(LocalDateTime.now());
        event.setContext(context);
        logEventRepository.save(event);
    }
}
