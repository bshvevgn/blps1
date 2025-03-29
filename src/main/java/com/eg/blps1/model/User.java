package com.eg.blps1.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    private String id;
    private String username;
    private String password;
    private RoleEnum role;

    public User(String username, String password, RoleEnum role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.role = role;
    }
}