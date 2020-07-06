/**
 * Copyright (c) 2016-2019 聚云科技 All rights reserved.
 * <p>
 * http://www.juiniot.com
 * <p>
 * 版权所有，侵权必究！
 */

package cn.hstc.recommend.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis-plus配置
 *
 * @author zehao
 */
@EnableTransactionManagement
@Configuration
@MapperScan("cn.hstc.recommend.dao")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * SQL执行效率插件
     */
    /*@Bean
    //@Profile({"dev", "test"})  设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }*/

    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
}
