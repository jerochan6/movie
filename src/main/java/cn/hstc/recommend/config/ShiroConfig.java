package cn.hstc.recommend.config;

import cn.hstc.recommend.utils.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * @Description //TODO 类里面声明了SecurityManager,DatabaseRealm, HashedCredentialsMatcher，ShiroFilterFactoryBean 等等东西
     * @Date 10:43 2020/7/6
     * @Param []
     * @return org.apache.shiro.spring.LifecycleBeanPostProcessor
     **/
    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean("securityManager")
    public SecurityManager securityManager(UserRealm userRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(null);

        return securityManager;
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
}
