package org.carshop.repositoriesTest;

import org.carshop.model.Role;
import org.carshop.model.User;
import org.carshop.repositories.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository userRepository;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        testUser = new User("1", "testUser", "password123", Role.CLIENT);
    }

    @Test
    void testSave() {
        userRepository.save(testUser);

        User foundUser = userRepository.findById("1");
        assertNotNull(foundUser, "User should be found");
        assertEquals("1", foundUser.getId(), "User ID should match");
        assertEquals("testUser", foundUser.getUsername(), "User username should match");
    }

    @Test
    void testFindById() {
        userRepository.save(testUser);

        User foundUser = userRepository.findById("1");
        assertNotNull(foundUser, "User should be found");
        assertEquals("1", foundUser.getId(), "User ID should match");
    }

    @Test
    void testFindByUsername() {
        userRepository.save(testUser);

        User foundUser = userRepository.findByUsername("testUser");
        assertNotNull(foundUser, "User should be found");
        assertEquals("testUser", foundUser.getUsername(), "User username should match");
    }

    @Test
    void testFindAll() {
        User user2 = new User("2", "testUser2", "password456", Role.ADMIN);
        userRepository.save(testUser);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size(), "There should be two users");
    }

    @Test
    void testUpdate() {
        userRepository.save(testUser);

        testUser.setUsername("updatedUser");
        testUser.setPassword("newPassword");
        userRepository.update(testUser);

        User updatedUser = userRepository.findById("1");
        assertNotNull(updatedUser, "User should be found");
        assertEquals("updatedUser", updatedUser.getUsername(), "User username should be updated");
        assertEquals("newPassword", updatedUser.getPassword(), "User password should be updated");
    }

    @Test
    void testDelete() {
        userRepository.save(testUser);

        userRepository.delete("1");
        User deletedUser = userRepository.findById("1");
        assertNull(deletedUser, "User should be deleted");
    }

    @Test
    void testCount() {
        User user2 = new User("2", "testUser2", "password456", Role.ADMIN);
        userRepository.save(testUser);
        userRepository.save(user2);

        assertEquals(2, userRepository.count(), "User count should be two");
    }
}