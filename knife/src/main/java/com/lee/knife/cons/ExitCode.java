package com.lee.knife.cons;

public enum ExitCode {

  ERROR_CODE(110,"error exit code"),
  EXCEPTION_CODE(120,"exception exit code"),
  CONSULT_CODE(114,"consult code");

  private int code;
  private String desc;

  private ExitCode(int code,String desc){
    this.code = code;
    this.desc = desc;
  }

  public int getCode(){
    return this.code;
  }

  public String getDesc() {
    return this.desc;
  }

}
