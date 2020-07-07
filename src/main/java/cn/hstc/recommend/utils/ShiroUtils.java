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
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.Date;
import java.util.UUID;

/**
 * Shiro工具类
 *
 */
public class ShiroUtils {
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

    public static UserEntity getUserEntity() {
        return (UserEntity)SecurityUtils.getSubject().getPrincipal();
    }

    public static Integer getUserId() {
        return getUserEntity().getId();
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
        SimpleHash simpleHash = new SimpleHash("MD5", userEntity.getPassword(), salt, 1024);
        userEntity.setPassword(simpleHash.toHex());
        userEntity.setSalt(salt);
        return userEntity;
    }


}
