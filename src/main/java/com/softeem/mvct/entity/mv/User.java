package com.softeem.mvct.entity.mv;


import java.time.LocalDateTime;
import java.util.Date;

public class User {

  private int id;
  private String username;
  private String password;
  private int role;
  private int level;
  private String phone;
  private String email;
  private LocalDateTime crtm;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public int getRole() {
    return role;
  }

  public void setRole(int role) {
    this.role = role;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDateTime getCrtm() {
    return crtm;
  }

  public void setCrtm(LocalDateTime crtm) {
    this.crtm = crtm;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", role=" + role +
            '}';
  }
}
