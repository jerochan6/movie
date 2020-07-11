package cn.hstc.recommend.utils;

/**
 * @ClassName ShiroUtils
 * @Description TODO
 * @Author zehao
 * @Date 2020/7/6 11:20
 * @Version 1.0
 **/

import cn.hstc.recommend.entity.UserEntity;
import cn.hstc.recommend.exception.RRException;
import cn.hstc.recommend.service.UserService;
import cn.hstc.recommend.service.impl.UserServiceImpl;
import org.apache.catalina.User;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Shiro工具类
 *
 */
@Component
public class ShiroUtils {

    @Autowired
    TokenHelp tokenHelp;

    /**  加密算法 */
    public final static String hashAlgorithmName = "SHA-256";
    /**  循环次数 */
    public final static int hashIterations = 16;




    public static String sha256(String password, String salt) {
        return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toString();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public  UserEntity getUserEntity() {

        String token = getSubject().getPrincipals().toString();

        UserEntity userEntity = tokenHelp.getUser(token);
        return userEntity;
    }

    public Integer getUserId() {
        UserEntity userEntity = getUserEntity();

        return userEntity.getId();
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public static String getKaptcha(String key) {
        Object kaptcha = getSessionAttribute(key);
        if(kaptcha == null){
            throw new RRException("验证码已失效");
        }
        getSession().removeAttribute(key);
        return kaptcha.toString();
    }

    public static UserEntity getShiroUser( UserEntity userEntity){
        String salt = UUID.randomUUID().toString().replaceAll("-","");
        SimpleHash simpleHash = new SimpleHash("MD5", userEntity.getPassword(), salt, Constant.HASHINTERATIONS);
        userEntity.setPassword(simpleHash.toHex());
        userEntity.setSalt(salt);
        return userEntity;
    }


}
