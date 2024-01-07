package br.com.brunno98.messageworker.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticmq.NodeAddress;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;

@Slf4j
@Getter
@Import(SqsServerConfig.class)
@TestComponent
public class SQSRestServerStarter {
    private static boolean running;

    @Autowired
    public SQSRestServerStarter(SqsServerConfig config) {
        log.debug("=== {} ===", getClass().getSimpleName());
        if (running) {
            log.debug("SQS Server already running! skiping...");
            return;
        }
        log.debug("Starting SQS Server");
        SQSRestServerBuilder
                .withPort(config.getPort())
                .withServerAddress(
                        NodeAddress.apply(
                                config.getProtocol(),
                                config.getHost(),
                                config.getPort(),
                                config.getContextPath()))
                .start();
        running = true;
    }

}
