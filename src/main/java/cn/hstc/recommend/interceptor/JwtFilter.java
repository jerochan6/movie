package cn.hstc.recommend.interceptor;

import cn.hstc.recommend.entity.UserEntity;
import cn.hstc.recommend.service.impl.JwtToken;
import cn.hstc.recommend.utils.Constant;
import cn.hstc.recommend.utils.ShiroUtils;
import cn.hstc.recommend.utils.TokenHelp;
import cn.hstc.recommend.utils.UserRealm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName JwtFilter
 * @Description TODO
 * @Author zehao
 * @Date 2020/7/7 10:21
 * @Version 1.0
 **/
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private TokenHelp tokenHelp;

    /**
     * 执行登录认证(判断请求头是否带上token)
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        logger.info("JwtFilter-->>>开始判断登录认证:init()");
        //如果请求头不存在token,则可能是执行登陆操作或是游客状态访问,直接返回true
        if (isLoginAttempt(request, response)) {
            return true;
        }
        //如果存在,则进入executeLogin方法执行登入,检查token 是否正确
            executeLogin(request, response);
            return true;
    }

    /**
     * 判断用户是否是登入,检测headers里是否包含token字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        logger.info("JwtFilter-->>>判断用户是否是登入:init()");
        HttpServletRequest req = (HttpServletRequest) request;
        // 从 http 请求头中取出 token
        String token = req.getHeader(Constant.ACESSTOKEN);
        if(null == token){
            return true;
        }
        // 验证 token
        return false;

    }

    /**
     * 重写AuthenticatingFilter的executeLogin方法丶执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        logger.info("JwtFilter-->>>开始进行登录验证:init()");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(Constant.ACESSTOKEN);//Access-Token
        JwtToken jwtToken = new JwtToken(token);
        //利用token验证账号是否可登录
        tokenHelp.tokenValidate(token);
        UserEntity userEntity = tokenHelp.getUser(token);
//        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userEntity.getUserName(),userEntity.getPassword());
        // 提交给realm进行登入,如果错误他会抛出异常并被捕获, 反之则代表登入成功,返回true
//        Subject subject = ShiroUtils.getSubject();
//        subject.login(jwtToken);
        getSubject(request, response).login(jwtToken);
        Constant.currentId = userEntity.getId();
        logger.info("JwtFilter-->>>用户[{}]验证成功",userEntity.getUserName());
        return true;
    }

  /*  *//**
     * 对跨域提供支持
     *//*
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        logger.info("JwtFilter-->>>preHandle-Method:init()");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }*/
}