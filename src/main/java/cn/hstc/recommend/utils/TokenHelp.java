package cn.hstc.recommend.utils;

import cn.hstc.recommend.entity.UserEntity;
import cn.hstc.recommend.exception.RRException;
import cn.hstc.recommend.interceptor.UserLoginToken;
import cn.hstc.recommend.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @ClassName TokenHelp
 * @Description TODO
 * @Author zehao
 * @Date 2020/5/29/029 15:21
 * @Version 1.0
 **/
@Component
public class TokenHelp {
    @Autowired
    private UserService userService;

    public boolean tokenValidate(String token){
            // 执行认证
            if (token == null) {
                throw new RRException("无token，请重新登录");
            }
            // 获取 token 中的 user id
            String userId;
            try {
                //验证token是否过期

                if(JWT.decode(token).getExpiresAt().compareTo(new Date()) < 0){
                    throw new RRException("登录已过期，请重新登录");
                }
                userId = JWT.decode(token).getAudience().get(0);
            } catch (JWTDecodeException j) {
                throw new RRException("401");
            }
            //改变当前登录Id
            Constant.currentId = Integer.parseInt(userId);
            UserEntity user = userService.getById(userId);
            //验证是否是管理员账号
            if (user == null) {
                throw new RRException("用户不存在，请重新登录");
            }
//            // 验证 token
//            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
//            try {
//                jwtVerifier.verify(token);
//            } catch (JWTVerificationException e) {
//                throw new RRException("401");
//            }
            return true;
    }

    public String getToken(UserEntity user) {
        String token="";
        token= JWT.create().withAudience(user.getId().toString())
                //设置token过期时间为一天
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    public UserEntity getUser(String token){
        String userId = JWT.decode(token).getAudience().get(0);
        UserEntity user = userService.getById(userId);
        return user;
    }
}
