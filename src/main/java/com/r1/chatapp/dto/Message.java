// Authors: Mirva ja Satu
// Viestiä kuvaava Message-luokka, getterit ja setterit

package com.r1.chatapp.dto;
import java.time.LocalDateTime;

public class Message {
    //Attribuutit:
    private int messageId;
    private String message;
    private String file;
    private LocalDateTime date;
    private int deleted;
    private int senderId;
    private int receiverId;
    // Omat yhteystiedot -toiminnallisuudelle lisätyt attribuutit
    // "contact" kuvaa sivupalkissa näkyvää yhteystietoa
    private int contactId; 
    private String contactName;
    private String datetime;

    // Konstruktorit:
    public Message() {
    }

    // Viestin konstruktori
    public Message(int messageId, String message, String file, LocalDateTime date, int deleted, int senderId, int receiverId) {
        this.messageId = messageId;
        this.message = message;
        this.file = file;
        this.date = date;
        this.deleted = deleted;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    // Konstruktori Omat yhteystiedot -toiminnallisuudelle:
    public Message(int message_id, String message, int contact_id, String contactName, String datetime) {
        this.messageId = message_id;
        this.message = message;
        this.contactId = contact_id;
        this.contactName = contactName;
        this.datetime = datetime;
    }

     // Konstruktori viestin lähety -toiminnallisuudelle, viesti ilman aikaleimaa:
     public Message(String message, int senderId, int receiverId) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        }

    // Konstruktori viestin lähety -toiminnallisuudelle, viesti aikaleimalla:
    public Message(String message, LocalDateTime date, int senderId, int receiverId) {
        this.message = message;
        this.date = date;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    // Getterit ja setterit:
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    // Omat yhteystiedot -toiminnallisuuden getterit ja setterit:
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
    
}