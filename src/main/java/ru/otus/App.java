package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.Message;
import ru.otus.domain.Human;
import ru.otus.domain.Soul;
import ru.otus.service.Death;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;


@IntegrationComponentScan
@ComponentScan
@Configuration
@EnableIntegration
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final Human[] HUMANS = {new Human("tall", new Soul("Sean")),
            new Human("ill", new Soul("Jack")),
            new Human("fat", new Soul("Ross")),
            new Human("sport", new Soul("Bob"))};
    private static final int TIMEOUT = 10;

    @Bean
    public QueueChannel humanChannel() {
        return MessageChannels.queue(4).get();
    }

    @Bean
    public QueueChannel heavenChannel() {
        return new QueueChannel();
    }

    @Bean
    public QueueChannel hellChannel() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow deathFlow() {
        return flow -> flow
                .split()
                .handle("deathService", "killTheGuy")
                .<Soul, Boolean>route(
                        Soul::isGood,
                        mapping -> mapping
                                .subFlowMapping(true, sf -> sf
                                        .channel("heavenChannel")
                                )
                                .subFlowMapping(false, sf -> sf
                                        .channel("hellChannel")
                                ));
    }

    public static void main(String[] args) {
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);
        QueueChannel heavenCh = ctx.getBean("heavenChannel", QueueChannel.class);
        QueueChannel hellCh = ctx.getBean("hellChannel", QueueChannel.class);

        logger.info( "Humans: " +
                getHumans().stream().map( human -> human.getSoul().getName() )
                        .collect( Collectors.joining( "," ) ) );

        Death death = ctx.getBean(Death.class);
        death.process(getHumans());

        Message<?> outMessage;
        Soul soul;
        while (heavenCh.getQueueSize() != 0){
            outMessage = heavenCh.receive(TIMEOUT);
            soul = (Soul) Objects.requireNonNull(outMessage).getPayload();
            logger.info(soul.getName() +
                    " is going to heaven. Good deeds: " + soul.getGoodDeeds() + ", bad deeds: " + soul.getBadDeeds());
        }

        while (hellCh.getQueueSize() != 0) {
            outMessage = hellCh.receive(TIMEOUT);
            soul = (Soul) Objects.requireNonNull(outMessage).getPayload();
            logger.info(soul.getName() +
                    " is going to hell. Good deeds: " + soul.getGoodDeeds() + ", bad deeds: " + soul.getBadDeeds());
        }
    }

    private static Collection<Human> getHumans() {
        return Arrays.stream(HUMANS).collect(Collectors.toList());
    }

}
