package com.punna.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final DiscoveryClient discoveryClient;

    @Bean
    @Primary
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {
        List<SwaggerUrl> swaggerUrls = new ArrayList<>();
        List<String> services = discoveryClient.getServices();

        for (String service : services) {
            discoveryClient
                    .getInstances(service)
                    .forEach(instance -> {
                        String url = instance
                                .getUri()
                                .toString() + "/v3/api-docs";
                        SwaggerUrl swaggerUrl = new SwaggerUrl();
                        swaggerUrl.setName(service);
                        swaggerUrl.setUrl(url);
                        swaggerUrls.add(swaggerUrl);
                    });
        }
        SwaggerUiConfigProperties properties = new SwaggerUiConfigProperties();
        if (properties.getUrls() == null) {
            properties.setUrls(new HashSet<>());
        }
        swaggerUrls.forEach(url -> properties
                .getUrls()
                .add(new SwaggerUiConfigProperties.SwaggerUrl(url.getName(),
                        url.getUrl(),
                        url.getDisplayName())));
        return properties;
    }
}
