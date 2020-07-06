package cn.hstc.recommend.config;

import cn.hstc.recommend.utils.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Description TODO
 * @Author zehao
 * @Date 2020/7/6 10:39
 * @Version 1.0
 **/
@Configuration
public class ShiroConfig {

    /**
     * @Author zehao
     * @Description //TODO 安全管理器，这个类组合了登陆，登出，权限，session的处理
     * @Date 15:24 2020/7/6
     * @Param [userRealm]
     * @return org.apache.shiro.mgt.SecurityManager
     **/
    @Bean("securityManager")
    public SecurityManager securityManager(UserRealm userRealm,SessionManager sessionManager) {
//        , SessionManager sessionManager
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);
//        securityManager.setRememberMeManager(null);

        return securityManager;
    }

    /**
     *单机环境，session交给shiro管理 用户的唯一标识，即Token或Authorization的认证
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionValidationInterval(3600 * 1000);
        sessionManager.setGlobalSessionTimeout(3600 * 1000);
        return sessionManager;
    }

    /**
     * @Author zehao
     * @Description //TODO 类里面声明了SecurityManager,DatabaseRealm, HashedCredentialsMatcher，ShiroFilterFactoryBean 等等东西
     * @Date 10:43 2020/7/6
     * @Param []
     * @return org.apache.shiro.spring.LifecycleBeanPostProcessor
     **/
    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * @Author zehao
     * @Description //TODO  开启shiro aop注解支持.使用代理方式;所以需要开启代码支持;
     * @Date 10:47 2020/7/6
     * @Param [securityManager]
     * @return org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
     **/
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        // 必须设置 SecurityManager
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
        //前后端分离项目中该链接应该为后端接口的访问url，通过该接口返回需要登录的状态码，由前端跳转至登录页面
        shiroFilter.setLoginUrl("/user/login");
        // 设置无权限时跳转的 url;
        shiroFilter.setUnauthorizedUrl("/");
//        //设置自定义filter
//        Map<String, Filter> map = new HashMap<>();
//        map.put("auth",authFilter);
//        shiroFilter.setFilters(map);
        // 设置拦截器
        Map<String, String> filterMap = new LinkedHashMap<>();
//        //anon:无参，开放权限，可以理解为匿名用户或游客
        filterMap.put("/user/login", "anon");
//        //其余接口一律拦截
//        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
//        //authc:无参，需要认证
        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }
}
