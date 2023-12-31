package br.com.brunno98.messagesender.integration;

import br.com.brunno98.messagesender.inbox.Inbox;
import br.com.brunno98.messagesender.inbox.InboxRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.awaitility.Durations;
import org.elasticmq.NodeAddress;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class MessageSenderIntegreationTest {

    @Autowired
    SqsTemplate sqsTemplate;

    @Autowired
    InboxRepository inboxRepository;

    @Test
    void whenConsumeFromQueueSholdSaveOnInboxBase() {
        sqsTemplate.send("send-message-queue", """
                {
                    "from": "foo",
                    "to": "bar",
                    "text": "Hello Text",
                    "requestId": "54fd4afe-be13-4359-8860-e0efcdf757f0"
                }
                """);

        await().atMost(Durations.ONE_SECOND);

        Optional<Inbox> inbox = inboxRepository.findByFromAndTo("foo", "bar");
        assertThat(inbox).isPresent();
        assertThat(inbox.get()).extracting(Inbox::getText)
                .isEqualTo("Hello Text");
    }
}
