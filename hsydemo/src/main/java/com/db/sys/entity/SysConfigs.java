package com.db.sys.entity;


import java.io.Serializable;

public class SysConfigs implements Serializable {

  private static final long serialVersionUID = 7901478214928784779L;
  private long id;
  private String name;
  private String value;
  private String note;
  private java.sql.Timestamp createdTime;
  private java.sql.Timestamp modifiedTime;
  private String createdUser;
  private String modifiedUser;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }


  public java.sql.Timestamp getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(java.sql.Timestamp createdTime) {
    this.createdTime = createdTime;
  }


  public java.sql.Timestamp getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(java.sql.Timestamp modifiedTime) {
    this.modifiedTime = modifiedTime;
  }


  public String getCreatedUser() {
    return createdUser;
  }

  public void setCreatedUser(String createdUser) {
    this.createdUser = createdUser;
  }


  public String getModifiedUser() {
    return modifiedUser;
  }

  public void setModifiedUser(String modifiedUser) {
    this.modifiedUser = modifiedUser;
  }

}
