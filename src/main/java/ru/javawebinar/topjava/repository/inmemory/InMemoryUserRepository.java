package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    //task 1 Имплементировать InMemoryUserRepository по аналогии с InMemoryMealRepository (список пользователей возвращать отсортированным по имени)

    //хранилище в виде мап
    private final Map<Integer, User> repositoryUser = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    //удаляем пользователя по id с типом ответа boolean
    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repositoryUser.remove(id) != null; //успешно удалили true, неуспешно false
    }

    //сохраняем пользователя если он новый, проверяем, если такой по id уже есть просто возвращаем его
    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.getAndIncrement());
            repositoryUser.put(user.getId(), user);
        }
        return user;
    }

    //получаем одного пользователя по id
    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repositoryUser.get(id);
    }

    //получаем всем пользователей из репо
    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repositoryUser.values().stream()
                .sorted(Comparator.comparing((User user) -> user.getName()).thenComparing(user -> user.getEmail())) //специально не буду сокращать тут код чтобы было видно, что иногда важно указывать тип переменной
                .collect(Collectors.toList());
    }

    //поиск пользователя по email
    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        //by cycle
//        User result = null;
//        for (Map.Entry<Integer, User> entry : repositoryUser.entrySet()) {
//            if (entry.getValue().getEmail().equals(email)){
//                result = entry.getValue();
//            }
//        }
//        return result;

        //by stream
        return repositoryUser.values().stream()
                .filter((User user) -> user.getEmail().equals(email)).findFirst().orElse(null);
    }
}
