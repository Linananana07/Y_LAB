package org.carshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Класс для представления аудита действий пользователя.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
    private String id;
    private User user;
    private String action;
    private Date date;
}
