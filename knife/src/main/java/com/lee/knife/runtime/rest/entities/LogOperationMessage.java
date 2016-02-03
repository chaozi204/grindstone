package com.lee.knife.runtime.rest.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created on 2016/1/29.
 */
public class LogOperationMessage {
  private String operation;
  private String message;
  private String level;
  private String status;

  @JsonCreator
  public LogOperationMessage(@JsonProperty("operation") String operation,
                              @JsonProperty("message") String message,
                              @JsonProperty("level") String level,
                              @JsonProperty("status") String status) {
    this.operation = operation;
    this.message = message;
    this.status = status;
    this.level = level;
  }

  @JsonProperty("operation")
  public String operation(){
    return operation;
  }

  @JsonProperty("message")
  public String message(){
    return message;
  }

  @JsonProperty("level")
  public String level(){
    return level;
  }

  @JsonProperty("status")
  public String status(){
    return status;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
