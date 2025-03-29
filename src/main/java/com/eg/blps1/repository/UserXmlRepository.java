package com.eg.blps1.repository;


import com.eg.blps1.model.User;
import com.eg.blps1.model.UserStorage;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.util.Optional;

@Repository
public class UserXmlRepository {
    private static final String FILE_PATH = "users.xml";

    public User save(User user) {
        UserStorage userStorage = loadUsers();
        userStorage.getUsers().add(user);
        saveUsers(userStorage);
        return user;
    }

    public Optional<User> findByUsername(String username) {
        UserStorage userStorage = loadUsers();
        return userStorage.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @SneakyThrows
    private UserStorage loadUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new UserStorage();
        }

        JAXBContext context = JAXBContext.newInstance(UserStorage.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (UserStorage) unmarshaller.unmarshal(file);
    }

    @SneakyThrows
    private void saveUsers(UserStorage userStorage) {
        JAXBContext context = JAXBContext.newInstance(UserStorage.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            marshaller.marshal(userStorage, writer);
        }
    }
}
