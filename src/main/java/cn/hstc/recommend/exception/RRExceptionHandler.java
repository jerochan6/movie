package cn.hstc.recommend.exception;

import cn.hstc.recommend.utils.Result;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义异常处理器
 *
 * @author Zero.
 */
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常RRException，改变其返回格式
	 */
	@ExceptionHandler(RRException.class)
	public Result handleRRException(RRException e){
		Result r = new Result();
		r.error(e.getCode(),e.getMessage());
		return r;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Result handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return new Result().error("数据库中已存在该记录");
	}

	/**
	 * 处理自定义异常UnauthorizedException，改变其返回格式
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public Result handleUnauthorizedException(UnauthorizedException e){
		Result r = new Result();
		logger.error(e.getMessage());
		r.error(500,"您没有该权限，请联系管理员获取！");
		return r;
	}


	@ExceptionHandler(Exception.class)
	public Result handleException(Exception e){
		logger.error(e.getMessage(), e);
		return new Result().error();
	}
}
