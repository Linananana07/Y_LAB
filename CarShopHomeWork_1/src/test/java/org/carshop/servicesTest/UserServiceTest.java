package org.carshop.servicesTest;

import org.carshop.model.Role;
import org.carshop.model.User;
import org.carshop.repositories.OrderRepository;
import org.carshop.repositories.UserRepository;
import org.carshop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        orderRepository = mock(OrderRepository.class);
        userService = new UserService(userRepository, orderRepository);
    }

    @Test
    public void testRegisterUserSuccess() {
        String username = "newuser";
        String password = "password";
        Role role = Role.CLIENT;

        when(userRepository.findByUsername(username)).thenReturn(null);
        when(userRepository.count()).thenReturn(1L);

        boolean result = userService.registerUser(username, password, role);

        assertTrue(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegisterUserFailure() {
        String username = "existinguser";
        String password = "password";
        Role role = Role.CLIENT;

        User existingUser = new User("1", username, password, role);
        when(userRepository.findByUsername(username)).thenReturn(existingUser);

        boolean result = userService.registerUser(username, password, role);

        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLoginSuccess() {
        String username = "user";
        String password = "password";
        User user = new User("1", username, password, Role.CLIENT);

        when(userRepository.findByUsername(username)).thenReturn(user);

        User result = userService.login(username, password);

        assertNotNull(result);
        assertEquals(user, result);
        assertEquals(user, userService.getCurrentUser());
    }

    @Test
    public void testLoginFailure() {
        String username = "user";
        String password = "wrongpassword";

        when(userRepository.findByUsername(username)).thenReturn(null);

        User result = userService.login(username, password);

        assertNull(result);
        assertNull(userService.getCurrentUser());
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User("1", "user1", "password", Role.CLIENT);
        User user2 = new User("2", "user2", "password", Role.ADMIN);
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(users, result);
    }

    @Test
    public void testGetUsersByName() {
        User user1 = new User("1", "user1", "password", Role.CLIENT);
        User user2 = new User("2", "user2", "password", Role.ADMIN);
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsersByName("user1");

        assertEquals(Collections.singletonList(user1), result);
    }

    @Test
    public void testGetUsersByRole() {
        User user1 = new User("1", "user1", "password", Role.CLIENT);
        User user2 = new User("2", "user2", "password", Role.CLIENT);
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsersByRole(Role.CLIENT);

        assertEquals(users, result);
    }

    @Test
    public void testSortUsersByPurchasesAscending() {
        User user1 = new User("1", "user1", "password", Role.CLIENT);
        user1.setPurchaseCount(5);
        User user2 = new User("2", "user2", "password", Role.CLIENT);
        user2.setPurchaseCount(10);
        List<User> users = Arrays.asList(user2, user1);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.sortUsersByPurchases(true);

        assertEquals(Arrays.asList(user1, user2), result);
    }

    @Test
    public void testUpdateAllUsers() {
        User user1 = new User("1", "user1", "password", Role.CLIENT);
        User updatedUser = new User("1", "updateduser", "newpassword", Role.ADMIN);

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user1));

        userService.updateAllUsers(updatedUser);

        verify(userRepository).update(any(User.class));
    }
}
