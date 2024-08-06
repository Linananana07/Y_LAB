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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User("1", "john_doe", "password123", Role.CLIENT);
        user2 = new User("2", "jane_doe", "password456", Role.MANAGER);
        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
    }

    @Test
    void testRegisterUserSuccess() {
        when(userRepository.findByUsername("new_user")).thenReturn(null);
        when(userRepository.count()).thenReturn(1L);
        doNothing().when(userRepository).save(any(User.class));

        boolean result = userService.registerUser("new_user", "new_password", Role.ADMIN);
        assertTrue(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUserFailure() {
        when(userRepository.findByUsername("john_doe")).thenReturn(user1);

        boolean result = userService.registerUser("john_doe", "password123", Role.CLIENT);
        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByUsername("john_doe")).thenReturn(user1);

        User loggedInUser = userService.login("john_doe", "password123");
        assertNotNull(loggedInUser);
        assertEquals("john_doe", loggedInUser.getUsername());
        assertEquals(user1, userService.getCurrentUser());
    }

    @Test
    void testLoginFailure() {
        when(userRepository.findByUsername("john_doe")).thenReturn(user1);

        User loggedInUser = userService.login("john_doe", "wrong_password");
        assertNull(loggedInUser);
        assertNull(userService.getCurrentUser());
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(userList);
        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("john_doe")).thenReturn(user1);
        User user = userService.findByUsername("john_doe");
        assertNotNull(user);
        assertEquals("john_doe", user.getUsername());
    }

    @Test
    void testDelete() {
        doNothing().when(userRepository).delete("1");
        userService.delete("1");
        verify(userRepository, times(1)).delete("1");
    }

    @Test
    void testUpdate() {
        doNothing().when(userRepository).update(any(User.class));
        user1.setUsername("john_updated");
        userService.update(user1);
        verify(userRepository, times(1)).update(user1);
    }

    @Test
    void testGetUsersByName() {
        when(userRepository.findAll()).thenReturn(userList);
        List<User> users = userService.getUsersByName("john_doe");
        assertEquals(1, users.size());
        assertEquals(user1, users.get(0));
    }

    @Test
    void testGetUsersByRole() {
        when(userRepository.findAll()).thenReturn(userList);
        List<User> users = userService.getUsersByRole(Role.CLIENT);
        assertEquals(1, users.size());
        assertEquals(user1, users.get(0));
    }

    @Test
    void testSortUsersByPurchasesAscending() {
        when(userRepository.findAll()).thenReturn(userList);
        List<User> sortedUsers = userService.sortUsersByPurchases(true);
        assertEquals(2, sortedUsers.size());
        assertEquals(user1, sortedUsers.get(0));
        assertEquals(user2, sortedUsers.get(1));
    }
}
