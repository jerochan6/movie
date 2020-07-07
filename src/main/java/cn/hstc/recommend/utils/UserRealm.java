package cn.hstc.recommend.utils;

import cn.hstc.recommend.dao.MenuDao;
import cn.hstc.recommend.dao.UserDao;
import cn.hstc.recommend.entity.MenuEntity;
import cn.hstc.recommend.entity.UserEntity;
import cn.hstc.recommend.service.impl.JwtToken;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * @ClassName UserRealm
 * @Description TODO
 * Shiro从从Realm获取安全数据（如用户、角色、权限）
 * ，就是说SecurityManager要验证用户身份，那么它需要从Realm获取相应
 * 的用户进行比较以确定用户身份是否合法；也需要从Realm得到用户相应的角色/权限进行验证用户是否能进行操作；
 * 可以把Realm看成DataSource，即安全数据源。Realm主要有两个方法：
 * doGetAuthorizationInfo(获取授权信息)
 * doGetAuthenticationInfo(获取身份验证相关信息)：
 * @Author zehao
 * @Date 2020/7/6 11:38
 * @Version 1.0
 **/
@Component("UserRealm")
public class UserRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);
    @Autowired
    public UserDao userDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    TokenHelp tokenHelp;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 授权(验证权限时调用)
     * 这个方法就会从数据库中读取我们所需要的信息，最后封装成SimpleAuthorizationInfo返回去
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(@org.jetbrains.annotations.NotNull PrincipalCollection principals) {
        //获取用户权限
        String token = (String) principals.getPrimaryPrincipal();
        UserEntity user = tokenHelp.getUser(token);
        Integer userId = user.getId();
//
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            List<MenuEntity> menuList = menuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for(MenuEntity menu : menuList){
                permsList.add(menu.getPermission());
            }
        }else{
            permsList = userDao.queryAllPerms(userId);
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
//
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        // 校验token有效性
        String token = (String) authcToken.getCredentials();
        UserEntity userEntity = tokenHelp.getUser(token);
        logger.info("对用户[{}]进行登录验证..验证开始",userEntity.getUserName());
//        //查询用户信息
//        UserEntity user = userDao.selectOne(new QueryWrapper<UserEntity>().
//                eq("user_name", userEntity.getUserName()));

//        //账号不存在
//        if(user == null) {
//            throw new UnknownAccountException("账号或密码不正确");
//        }


        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(token,token,"UserRealm");

        return info;
    }

//    @Override
//    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
//        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
//        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
//        shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
//        super.setCredentialsMatcher(shaCredentialsMatcher);
//    }
}
