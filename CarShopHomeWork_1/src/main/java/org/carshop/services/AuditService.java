package org.carshop.services;

import org.carshop.model.Audit;
import org.carshop.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AuditService {
    private List<Audit> auditLogs = new ArrayList<>();

    public void logAction(User user, String action) {
        Audit audit = new Audit(generateId(), user, action, new Date());
        auditLogs.add(audit);
    }

    public List<Audit> getAuditLogs() {
        return new ArrayList<>(auditLogs);
    }

    public List<Audit> getAuditLogsByUser(User user) {
        return auditLogs.stream().filter(audit -> audit.getUser().equals(user)).collect(Collectors.toList());
    }

    public List<Audit> getAuditLogsByDate(Date date) {
        return auditLogs.stream().filter(audit -> audit.getDate().equals(date)).collect(Collectors.toList());
    }

    private String generateId() {
        return String.valueOf(auditLogs.size() + 1);
    }
}
