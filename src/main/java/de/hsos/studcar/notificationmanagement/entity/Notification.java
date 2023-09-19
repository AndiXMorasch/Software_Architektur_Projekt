package de.hsos.studcar.notificationmanagement.entity;

public class Notification {

    private Long id;

    private String receiverId;

    private String headline;

    private String message;

    private String date;

    private boolean read;

    public Notification() {
        this.read = false;
    }

    public Notification(Long id, String receiverId, String headline, String message, String date, boolean read) {
        this.id = id;
        this.receiverId = receiverId;
        this.headline = headline;
        this.message = message;
        this.date = date;
        this.read = read;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean wasRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
