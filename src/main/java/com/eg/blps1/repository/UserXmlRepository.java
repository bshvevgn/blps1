package com.eg.blps1.repository;


import com.eg.blps1.model.User;
import com.eg.blps1.model.UserStorage;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserXmlRepository {
    private static final String FILE_PATH = "users.xml";
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

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

    private UserStorage loadUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new UserStorage();
        }
        if (!file.canRead() || !file.canWrite()) throw new RuntimeException("Файл закрыт для чтения или записи.");

        try {
            return (UserStorage) unmarshaller.unmarshal(file);
        } catch (JAXBException ex) {
            throw new RuntimeException("Ошибка при загрузке данных пользователей!");
        }
    }

    private void saveUsers(UserStorage userStorage) {
        try {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                marshaller.marshal(userStorage, writer);
            }
        } catch (JAXBException | IOException ex) {
            throw new RuntimeException("Ошибка при сохранении данных пользователей!");
        }
    }
}
