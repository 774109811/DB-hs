package com.db.common.exception;
/**
 * 自定义异常?(继承RuntimeException或者Exception)
 * 为什么自定义异常？
 * 1)能够更好对异常信息进行理解和定位
 * 2)提高处理异常的速度和质量。
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -592852566456285553L;

	public ServiceException() {
		super();
	}
	public ServiceException(String message) {
		super(message);
	}
	public ServiceException(Throwable cause) {
		super(cause);
	}

	
}
