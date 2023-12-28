package br.com.brunno98.messageworker.config;

import lombok.Getter;
import org.elasticmq.NodeAddress;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;

@Getter
@Import(SqsServerConfig.class)
@TestComponent
public class SQSRestServerStarter {

    @Autowired
    public SQSRestServerStarter(SqsServerConfig config) {
            SQSRestServerBuilder
                .withPort(config.getPort())
                .withServerAddress(
                        NodeAddress.apply(
                                config.getProtocol(),
                                config.getHost(),
                                config.getPort(),
                                config.getContextPath()))
                .start();
    }

}
