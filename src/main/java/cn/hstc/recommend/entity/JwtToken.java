package cn.hstc.recommend.entity;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName JwtToken
 * @Description TODO
 * @Author zehao
 * @Date 2020/7/7 10:20
 * @Version 1.0
 **/
public class JwtToken implements AuthenticationToken {
//    private static final long serialVersionUID = -8451637096112402805L;
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
    @Override
    public Object getCredentials() {
        return token;
    }


}
