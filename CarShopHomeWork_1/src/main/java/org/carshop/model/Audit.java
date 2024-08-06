package org.carshop.model;

import java.util.Date;

public class Audit {
    private String id;
    private User user;
    private String action;
    private Date date;

    public Audit(String id, User user, String action, Date date) {
        this.id = id;
        this.user = user;
        this.action = action;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
