package org.carshop.modelTest;

import org.carshop.model.Role;
import org.carshop.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    public void testUserConstructorAndGetters() {
        User user = new User("1", "username", "password", Role.CLIENT);

        assertEquals("1", user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals(Role.CLIENT, user.getRole());
        assertEquals(0, user.getPurchaseCount());
    }

    @Test
    public void testSetters() {
        User user = new User("1", "username", "password", Role.CLIENT);

        user.setId("2");
        user.setUsername("newUsername");
        user.setPassword("newPassword");
        user.setRole(Role.MANAGER);
        user.setPurchaseCount(10);

        assertEquals("2", user.getId());
        assertEquals("newUsername", user.getUsername());
        assertEquals("newPassword", user.getPassword());
        assertEquals(Role.MANAGER, user.getRole());
        assertEquals(10, user.getPurchaseCount());
    }
}
