package org.carshop.servicesTest;

import org.carshop.model.Audit;
import org.carshop.model.Role;
import org.carshop.model.User;
import org.carshop.services.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuditServiceTest {

    private AuditService auditService;
    private User testUser;
    private Date testDate;

    @BeforeEach
    void setUp() {
        auditService = new AuditService();
        testUser = new User("1", "testUser", "password123", Role.CLIENT);
        testDate = new Date(); // Инициализируем текущей датой
    }

    @Test
    void testLogAction() {
        auditService.logAction(testUser, "Login");

        List<Audit> auditLogs = auditService.getAuditLogs();
        assertEquals(1, auditLogs.size(), "There should be one audit log");
        Audit audit = auditLogs.get(0);
        assertNotNull(audit, "Audit log should not be null");
        assertEquals(testUser, audit.getUser(), "User should match");
        assertEquals("Login", audit.getAction(), "Action should match");
        assertNotNull(audit.getDate(), "Date should not be null");
    }

    @Test
    void testGetAuditLogsByUser() {
        auditService.logAction(testUser, "Login");
        User anotherUser = new User("2", "anotherUser", "password456", Role.ADMIN);
        auditService.logAction(anotherUser, "Logout");

        List<Audit> userLogs = auditService.getAuditLogsByUser(testUser);
        assertEquals(1, userLogs.size(), "There should be one audit log for the user");
        Audit audit = userLogs.get(0);
        assertEquals(testUser, audit.getUser(), "User should match");
    }

    @Test
    void testGetAuditLogsByDate() {
        // Используем фиксированную дату для тестирования
        Date fixedDate = truncateDateToDay(new Date());

        // Логируем действия с фиксированной датой
        auditService.logAction(testUser, "Login");
        auditService.logAction(testUser, "Logout");

        // Переопределяем дату для записей, чтобы использовать фиксированную
        List<Audit> allLogs = auditService.getAuditLogs();
        for (Audit audit : allLogs) {
            audit.setDate(fixedDate); // Устанавливаем фиксированную дату
        }

        // Получаем записи по дате
        List<Audit> dateLogs = auditService.getAuditLogsByDate(fixedDate);
        assertEquals(2, dateLogs.size(), "There should be two audit logs for the date");

        // Проверяем, что каждая запись имеет правильную дату
        for (Audit audit : dateLogs) {
            Date auditDateWithoutTime = truncateDateToDay(audit.getDate());
            assertEquals(fixedDate, auditDateWithoutTime, "Date should match");
        }
    }

    @Test
    void testGenerateId() {
        auditService.logAction(testUser, "Login");
        List<Audit> auditLogs = auditService.getAuditLogs();
        assertEquals(1, auditLogs.size(), "There should be one audit log");
        assertEquals("1", auditLogs.get(0).getId(), "Audit ID should be 1");

        auditService.logAction(testUser, "Logout");
        auditLogs = auditService.getAuditLogs();
        assertEquals(2, auditLogs.size(), "There should be two audit logs");
        assertEquals("2", auditLogs.get(1).getId(), "Audit ID should be 2");
    }

    private Date truncateDateToDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
