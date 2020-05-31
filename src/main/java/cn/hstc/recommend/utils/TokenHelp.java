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

    public boolean tokenValidate(String token,boolean isAdmin){
            // 执行认证
            if (token == null) {
                throw new RRException("无token，请重新登录");
            }
            // 获取 token 中的 user id
            String userId;
            try {
                userId = JWT.decode(token).getAudience().get(0);
            } catch (JWTDecodeException j) {
                throw new RRException("401");
            }
            UserEntity user = userService.getById(userId);
            //验证是否是管理员账号
            if(isAdmin){
                if(!user.getUserName().equals(Constant.SUPER_ADMIN_NAME)){
                    throw new RRException("该操作需要有管理员权限");
                }
            }
            if (user == null) {
                throw new RRException("用户不存在，请重新登录");
            }
            // 验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            try {
                jwtVerifier.verify(token);
            } catch (JWTVerificationException e) {
                throw new RRException("401");
            }
            return true;
    }

    public String getToken(UserEntity user) {
        String token="";
        token= JWT.create().withAudience(user.getId().toString())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
