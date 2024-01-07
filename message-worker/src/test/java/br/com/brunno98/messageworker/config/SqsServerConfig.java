package br.com.brunno98.messageworker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@Data
@TestConfiguration
@ConfigurationProperties(prefix = "application.sqs.server")
public class SqsServerConfig {
    private String protocol;
    private String host;
    private Integer port;
    private String contextPath;
}
