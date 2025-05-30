package unir.reto.talenthub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TalentHubApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TalentHubApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TalentHubApplication.class);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                      .allowedOrigins("https://anagonbu.com")
                      .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                      .allowedHeaders("*")
                      .allowCredentials(true);
            }
        };
    }
}
