package com.db.common.vo;
import java.io.Serializable;
/**
 * 用于封装控制层要输出到
 * 客户端的数据
 * 1)正常数据
 * 2)异常数据(业务异常，系统异常)
 */
public class JsonResult implements Serializable{
	 private static final long serialVersionUID = -4138033536625725437L;
	/**状态码*/
     private int state=1;//1-OK,0-Error
     /**状态码对应的消息*/
     private String message="OK";
     /**要呈现到客户端的业务数据*/
     private Object data;
     public JsonResult() {
	 }
     public JsonResult(String message) {
    	 this.message=message;
	 }
     public JsonResult(Object data) {
    	 this.data=data;
     }
     public JsonResult(Throwable e) {
    	 this.state=0;
    	 this.message=e.getMessage();
     }
     
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}







