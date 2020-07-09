package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.exception.RRException;
import cn.hstc.recommend.utils.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.dao.UserDao;
import cn.hstc.recommend.entity.UserEntity;
import cn.hstc.recommend.service.UserService;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {


    private TokenHelp tokenHelp = new TokenHelp();


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public Result loginValidate(String userName, String password) {
        //查找是否存在该用户名
        UserEntity userEntity = this.baseMapper.selectOne(new QueryWrapper<UserEntity>()
                .eq("user_name", userName));
        //若不存在，提示用户登录失败
        if (null == userEntity) {
            return new Result().error("该账号未注册，请进行注册！");
        }

        //我的密码是使用uuid作为盐值加密的，所以这里登陆时候还需要做一次对比
        SimpleHash simpleHash = new SimpleHash("MD5", password,  userEntity.getSalt(),
                Constant.HASHINTERATIONS);
        if(!simpleHash.toHex().equals(userEntity.getPassword())){
            return new Result().error("密码不正确");
        }
        String token = tokenHelp.getToken(userEntity);
        Result result = new Result<>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", token);
        map.put("user", userEntity);
        return result.ok(map);
    }

    @Override
    public boolean updateById(UserEntity userEntity) {

        userEntity.setId(Constant.currentId);
        userEntity.setUpdateTime(new Date());
        String result = this.REXValidate(userEntity);
        if (result != null) {
            throw new RRException(result);
        }
        //获取sha256加密后的user
        UserEntity shiroUser = ShiroUtils.getShiroUser(userEntity);
        return this.retBool(this.baseMapper.updateById(shiroUser));
    }

    @Override
    public boolean save(UserEntity userEntity) {
        //设置创建时间
        userEntity.setCreateTime(new Date());
        //验证格式
        String result = this.REXValidate(userEntity);

        if (result != null) {
            throw new RRException(result);
        }
        //获取sha256加密后的user
        UserEntity shiroUser = ShiroUtils.getShiroUser(userEntity);


        return this.retBool(this.baseMapper.insert(shiroUser));
    }

    private String REXValidate(UserEntity userEntity) {



        if (null != userEntity.getUserName()) {
            String username = userEntity.getUserName();
            if (!username.matches(Constant.USERNAMERRE)) {
                return "登录名：只能输入5-20个以字母开头、可带数字、“_”、“.”的字串 ";
            }
            //检查是否已存在该账户
            UserEntity user = this.baseMapper.selectOne(new QueryWrapper<UserEntity>()
                    .eq("user_name", userEntity.getUserName()));

            if(user != null){
                if (userEntity.getId() == null) {
                    return "该用户名已存在";
                }
                if(null != userEntity.getId() && !userEntity.getId().equals(user.getId())){
                    if(user.getUserName().equals(userEntity.getUserName())){
                        return "该用户名已存在";
                    }
                }
            }


        }
        if (null != userEntity.getPassword()) {
            String password = userEntity.getPassword();
            if (!password.matches(Constant.PSWREX)) {
                return "密码只能为6-20个字母、数字、下划线 ";
            }
        }
        if (null != userEntity.getPhone()) {
            String phone = userEntity.getPhone();
            if (!phone.matches(Constant.MOBILEPHONEREX)) {
                return "请输入正确的手机号";
            }
            //检查手机号是否已注册
            UserEntity user = this.baseMapper.selectOne(new QueryWrapper<UserEntity>()
                    .eq("phone", userEntity.getPhone()));
            if(user != null){
                if ( userEntity.getId() == null) {
                    return "该手机号已注册";
                }
                if(null != userEntity.getId() && !userEntity.getId().equals(user.getId())){
                    if(user.getPhone().equals(userEntity.getPhone())){
                        return "该手机号已注册";
                    }
                }
            }


        }
        return null;
    }

}
