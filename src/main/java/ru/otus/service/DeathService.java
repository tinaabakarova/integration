package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.domain.Human;
import ru.otus.domain.Soul;

@Service
public class DeathService {
    private static final Logger logger = LoggerFactory.getLogger(DeathService.class);

    public Soul killTheGuy(Human human) {
        logger.info("Killing..... " + human.getSoul().getName());
        return human.getSoul();
    }
}
