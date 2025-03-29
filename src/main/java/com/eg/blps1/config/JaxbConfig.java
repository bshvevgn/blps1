package com.eg.blps1.config;

import com.eg.blps1.model.UserStorage;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class JaxbConfig {

    @Bean
    public Unmarshaller unmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(UserStorage.class);
        return context.createUnmarshaller();
    }

    @Bean
    public Marshaller marshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(UserStorage.class);
        return context.createMarshaller();
    }
}
