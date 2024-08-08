package org.carshop.consoleTest;

import org.carshop.console.*;
import org.carshop.model.Role;
import org.carshop.model.User;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainDashboardTest {

    @Mock
    private UserService userService;
    @Mock
    private CarService carService;
    @Mock
    private OrderService orderService;
    @Mock
    private AuditService auditService;

    @InjectMocks
    private MainDashboard mainDashboard;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        assertNotNull(mainDashboard, "mainDashboard should be initialized");
    }

    @Test
    public void testAdminDashboard() throws Exception {
        User admin = new User("1", "admin", "password", Role.ADMIN);

        Method method = MainDashboard.class.getDeclaredMethod("adminDashboard", User.class);
        method.setAccessible(true);

    }

    @Test
    public void testManagerDashboard() throws Exception {
        User manager = new User("1", "manager", "password", Role.MANAGER);

        Method method = MainDashboard.class.getDeclaredMethod("managerDashboard", User.class);
        method.setAccessible(true);

    }
}
