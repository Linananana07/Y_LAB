package org.carshop.consoleTest;

import org.carshop.console.AuditDashboard;
import org.carshop.console.MainDashboard;
import org.carshop.model.Audit;
import org.carshop.model.Role;
import org.carshop.model.User;
import org.carshop.services.AuditService;
import org.carshop.services.CarService;
import org.carshop.services.OrderService;
import org.carshop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuditDashboardTest {

    @Mock
    private UserService userService;

    @Mock
    private CarService carService;

    @Mock
    private OrderService orderService;

    @Mock
    private AuditService auditService;

    @Mock
    private MainDashboard mainDashboard;

    @InjectMocks
    private AuditDashboard auditDashboard;

    private User currentUser;
    private List<Audit> auditLogs;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currentUser = new User("1", "john_doe", "password123", Role.CLIENT);

        auditLogs = new ArrayList<>();
        auditLogs.add(new Audit("1", currentUser, "Action1", new Date()));
        auditLogs.add(new Audit("2", new User("2", "jane_doe", "password456", Role.MANAGER), "Action2", new Date()));
    }

    @Test
    void testDisplayAuditLog() throws Exception {
        // Создаем mock System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Используем Reflection для вызова приватного метода
        invokePrivateMethod("displayAuditLog", auditLogs.get(0));

        String expectedOutput = "ID: 1\n" +
                "Пользователь: john_doe\n" +
                "Действие: Action1\n" +
                "Дата: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(auditLogs.get(0).getDate());
        assertEquals(expectedOutput, outputStream.toString().trim());

        // Восстанавливаем System.out
        System.setOut(originalOut);
    }

    private void invokePrivateMethod(String methodName, Object... parameters) throws Exception {
        Method method = AuditDashboard.class.getDeclaredMethod(methodName, getParameterTypes(parameters));
        method.setAccessible(true);
        method.invoke(auditDashboard, parameters);
    }

    private Class<?>[] getParameterTypes(Object... parameters) {
        Class<?>[] types = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            types[i] = parameters[i].getClass();
        }
        return types;
    }
}
