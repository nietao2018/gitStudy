package com.example.entrytask.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseEntity {

    @SerializedName("err_code")
    @Expose
    private int err_code;
    @SerializedName("err_msg")
    @Expose
    private String err_msg;
    @SerializedName("data")
    @Expose
    private DataEntity data;

    public ResponseEntity(int err_code, String err_msg, DataEntity dataBean){
        this.err_code = err_code;
        this.err_msg = err_msg;
        this.data = dataBean;
    }

    public void setErr_code(int err_code){
        this.err_code = err_code;
    }
    public int getErr_code(){
        return this.err_code;
    }
    public void setErr_msg(String err_msg){
        this.err_msg = err_msg;
    }
    public String getErr_msg(){
        return this.err_msg;
    }
    public void setData(DataEntity data){
        this.data = data;
    }
    public DataEntity getData(){
        return this.data;
    }


}
