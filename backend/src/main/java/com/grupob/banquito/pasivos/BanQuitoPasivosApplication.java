package com.grupob.banquito.pasivos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BanQuitoPasivosApplication {

    public static void main(String[] argumentos) {
        SpringApplication.run(BanQuitoPasivosApplication.class, argumentos);
    }

    @Bean
    public WebMvcConfigurer configuradorCors() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registro) {
                registro.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173", "http://localhost:5174")
                        .allowedMethods("GET", "POST", "OPTIONS");
            }
        };
    }
}