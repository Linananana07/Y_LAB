package org.carshop.repositoriesTest;

import org.carshop.model.Role;
import org.carshop.model.User;
import org.carshop.repositories.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {
    private InMemoryUserRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    public void testSaveUser() {
        User user = new User("1", "john_doe", "password123", Role.CLIENT);
        repository.save(user);

        User foundUser = repository.findById("1");
        assertNotNull(foundUser, "User should be found after saving");
        assertEquals("john_doe", foundUser.getUsername(), "Username should match");
    }

    @Test
    public void testFindByUsername() {
        User user = new User("2", "jane_doe", "password456", Role.MANAGER);
        repository.save(user);

        User foundUser = repository.findByUsername("jane_doe");
        assertNotNull(foundUser, "User should be found by username");
        assertEquals("jane_doe", foundUser.getUsername(), "Username should match");
    }

    @Test
    public void testFindAll() {
        User user1 = new User("3", "alice", "password789", Role.ADMIN);
        User user2 = new User("4", "bob", "password000", Role.CLIENT);
        repository.save(user1);
        repository.save(user2);

        List<User> allUsers = repository.findAll();
        assertEquals(2, allUsers.size(), "There should be 2 users in the repository");
    }

    @Test
    public void testUpdateUser() {
        User user = new User("5", "carol", "password111", Role.MANAGER);
        repository.save(user);

        User updatedUser = new User("5", "carol_updated", "password222", Role.CLIENT);
        repository.update(updatedUser);

        User foundUser = repository.findById("5");
        assertNotNull(foundUser, "User should be found after update");
        assertEquals("carol_updated", foundUser.getUsername(), "Username should be updated");
        assertEquals(Role.CLIENT, foundUser.getRole(), "Role should be updated");
    }

    @Test
    public void testDeleteUser() {
        User user = new User("6", "dave", "password333", Role.ADMIN);
        repository.save(user);
        repository.delete("6");

        User foundUser = repository.findById("6");
        assertNull(foundUser, "User should be null after deletion");
    }

    @Test
    public void testCount() {
        User user1 = new User("7", "eve", "password444", Role.MANAGER);
        User user2 = new User("8", "frank", "password555", Role.CLIENT);
        repository.save(user1);
        repository.save(user2);

        long count = repository.count();
        assertEquals(2, count, "There should be 2 users in the repository");
    }
}