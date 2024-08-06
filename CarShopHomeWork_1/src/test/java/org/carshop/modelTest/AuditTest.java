package org.carshop.modelTest;

import org.carshop.model.Audit;
import org.carshop.model.Role;
import org.carshop.model.User;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;

public class AuditTest {

    @Test
    public void testAuditConstructorAndGetters() {
        User user = new User("1", "admin", "password", Role.ADMIN);
        Date date = new Date();
        Audit audit = new Audit("1", user, "Login Attempt", date);

        assertEquals("1", audit.getId());
        assertEquals(user, audit.getUser());
        assertEquals("Login Attempt", audit.getAction());
        assertEquals(date, audit.getDate());
    }

    @Test
    public void testSetters() {
        User user = new User("1", "admin", "password", Role.ADMIN);
        Date date = new Date();
        Audit audit = new Audit("1", user, "Login Attempt", date);

        User newUser = new User("2", "user", "password", Role.CLIENT);
        Date newDate = new Date(date.getTime() + 100000); // изменяем дату

        audit.setId("2");
        audit.setUser(newUser);
        audit.setAction("Purchase");
        audit.setDate(newDate);

        assertEquals("2", audit.getId());
        assertEquals(newUser, audit.getUser());
        assertEquals("Purchase", audit.getAction());
        assertEquals(newDate, audit.getDate());
    }
}
