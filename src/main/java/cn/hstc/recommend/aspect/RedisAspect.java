package cn.hstc.recommend.aspect;

import cn.hstc.recommend.exception.RRException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName RedisAspect
 * @Description TODO Redis切面处理类
 * @Author zehao
 * @Date 2020/7/5 20:24
 * @Version 1.0
 **/
@Aspect
@Component
public class RedisAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 是否开启redis缓存  true开启   false关闭
     */
    @Value("${redis.open}")
    private boolean open;

    @Around("execution(* cn.hstc.recommend.service.RedisService.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        //判断是否开启了redis
        if (open) {
            try {
                result = point.proceed();
//                point.proceed();
            } catch (Exception e) {
                logger.error("redis error", e);
                throw new RRException("Redis服务异常");
            }
        }
        return result;
    }



}
