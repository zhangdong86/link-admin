package com.link.admin.core.web.mvc;

/**
 * 
* @ClassName: ResponseResult 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 252956
* @date 2019年10月21日 下午4:53:02 
*
 */
public class ResponseResult {

	private int code = 20000;
	private String msg;
	private Object result;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
