package pack.gateway.security;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerApiConfig {

    @Bean
    public GroupedOpenApi userServiceApi() {
        return GroupedOpenApi.builder()
                .group("user-service")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi courseServiceApi() {
        return GroupedOpenApi.builder()
                .group("course-service")
                .pathsToMatch("/courses/**")
                .build();
    }

    @Bean
    public GroupedOpenApi activityServiceApi() {
        return GroupedOpenApi.builder()
                .group("activity-service")
                .pathsToMatch("/activities/**")
                .build();
    }

    @Bean
    public GroupedOpenApi analyticsServiceApi() {
        return GroupedOpenApi.builder()
                .group("analytics-service")
                .pathsToMatch("/analytics/**")
                .build();
    }
}
