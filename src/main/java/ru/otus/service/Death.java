package ru.otus.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.domain.Human;

import java.util.Collection;

@MessagingGateway
public interface Death {

    @Gateway(requestChannel = "deathFlow.input")
    void process(Collection<Human> humans);

}
