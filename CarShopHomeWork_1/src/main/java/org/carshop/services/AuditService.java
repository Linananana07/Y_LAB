package org.carshop.services;

import org.carshop.model.Audit;
import org.carshop.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с журналом аудита.
 * <p>Этот сервис управляет логами аудита, включая запись действий и получение логов по пользователю или дате.</p>
 */
public class AuditService {
    private List<Audit> auditLogs = new ArrayList<>();

    /**
     * Записывает действие в журнал аудита.
     *
     * @param user пользователь, совершивший действие
     * @param action описание действия
     */
    public void logAction(User user, String action) {
        Audit audit = new Audit(generateId(), user, action, new Date());
        auditLogs.add(audit);
    }

    /**
     * Возвращает список всех логов аудита.
     *
     * @return список всех логов аудита
     */
    public List<Audit> getAuditLogs() {
        return new ArrayList<>(auditLogs);
    }

    /**
     * Возвращает список логов аудита для указанного пользователя.
     *
     * @param user пользователь, для которого нужно получить логи
     * @return список логов аудита для указанного пользователя
     */
    public List<Audit> getAuditLogsByUser(User user) {
        return auditLogs.stream().filter(audit -> audit.getUser().equals(user)).collect(Collectors.toList());
    }

    /**
     * Возвращает список логов аудита для указанной даты.
     *
     * @param date дата, для которой нужно получить логи
     * @return список логов аудита для указанной даты
     */
    public List<Audit> getAuditLogsByDate(Date date) {
        return auditLogs.stream().filter(audit -> audit.getDate().equals(date)).collect(Collectors.toList());
    }

    /**
     * Генерирует уникальный идентификатор для нового лога аудита.
     *
     * @return уникальный идентификатор
     */
    private String generateId() {
        return String.valueOf(auditLogs.size() + 1);
    }
}
