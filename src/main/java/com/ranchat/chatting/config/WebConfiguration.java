package com.ranchat.chatting.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LocalDateConverter());
        registry.addConverter(new LocalDateTimeConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 특정 파일에 대한 접근만 허용
        registry.addResourceHandler("/index.html")
            .addResourceLocations("classpath:/static/index.html");

        registry.addResourceHandler("/api-docs/ranchat/main.json")
            .addResourceLocations("classpath:/static/");

        registry.addResourceHandler(
                "/docs.html",
                "/css/asyncapi.min.css",
                "/css/global.min.css",
                "/js/asyncapi-ui.min.js"
            )
            .addResourceLocations("classpath:/static/websocket-docs/");

        // 그 외 리소스 요청을 차단하기 위해 빈 핸들러 추가
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(new ResourceResolver() {
                @Override
                public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
                    return null;
                }

                @Override
                public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
                    return "";
                }
            });
    }

    static class LocalDateConverter implements Converter<String, LocalDate> {
        public LocalDate convert(String source) {
            return LocalDate.parse(source, DateTimeFormatter.ISO_DATE);
        }
    }

    static class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
        public LocalDateTime convert(String source) {
            return LocalDateTime.parse(source, DateTimeFormatter.ISO_DATE_TIME);
        }
    }
}
