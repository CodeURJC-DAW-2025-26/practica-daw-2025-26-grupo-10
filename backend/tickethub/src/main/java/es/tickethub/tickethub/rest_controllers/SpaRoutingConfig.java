package es.tickethub.tickethub.rest_controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class SpaRoutingConfig implements WebMvcConfigurer {
    private static final String SPA_ROUTE = "/new";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(SPA_ROUTE, SPA_ROUTE + "/**")
                .addResourceLocations("classpath:/static" + SPA_ROUTE + "/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        return requestedResource.exists() && requestedResource.isReadable()
                                ? requestedResource
                                : new ClassPathResource("/static" + SPA_ROUTE + "/index.html");
                    }
                });
    }
}