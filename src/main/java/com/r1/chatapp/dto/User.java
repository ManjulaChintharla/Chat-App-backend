// Author: Satu
// Käyttäjää kuvaava User-luokka, getterit ja setterit

package com.r1.chatapp.dto;

public class User {

    // Attribuutit:
    private int user_id;
    private String name;
    private String username; // on käyttäjän sähköpostiosoite
    private String password;
    private String newPassword;
    private String picture;
    private String description;

    // Konstruktorit:
    public User() {
    }

    // Käyttäjän konstruktori 
    public User(int user_id, String name, String username, String password, String picture, String description) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.picture = picture;
        this.description = description;
    }
    
    // Konstruktori Rekisteröitymien -toiminnallisuudelle
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // Konstruktori Sisäänkirjautuminen -toiminnallisuudelle
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getterit ja setterit:

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
