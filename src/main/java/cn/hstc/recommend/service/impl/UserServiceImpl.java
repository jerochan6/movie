package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.exception.RRException;
import cn.hstc.recommend.utils.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
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
        //验证账号和密码是否正确
//        if (userEntity.getUserName().equals(userName) &&
//                userEntity.getPassword().equals(password)) {

        //登录成功，保存token

        String token = tokenHelp.getToken(userEntity);
        Result result = new Result<>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", token);
        map.put("user", userEntity);

//        try {
//            Subject subject = ShiroUtils.getSubject();
//            UsernamePasswordToken token2 = new UsernamePasswordToken(userName, password);
//            subject.login(token2);
//        } catch (UnknownAccountException e) {
//            return new Result().error(e.getMessage());
//        } catch (IncorrectCredentialsException e) {
//            return new Result().error("账号或密码不正确!");
//        } catch (LockedAccountException e) {
//            return new Result().error("账号已被锁定,请联系管理员!");
//        } catch (AuthenticationException e) {
//            e.printStackTrace();
//            return new Result().error("账户验证失败!");
//        }

        return result.ok(map);
//        }
//        return new Result().error("登录失败，请检查账号和密码！");
    }

    @Override
    public boolean updateById(UserEntity userEntity) {
        userEntity.setId(Constant.currentId);
        userEntity.setUpdateTime(new Date());
        String result = this.REXValidate(userEntity);
        if (result != null) {
            throw new RRException(result);
        }
        return this.retBool(this.baseMapper.updateById(userEntity));
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
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        userEntity.setSalt(salt);
        userEntity.setPassword(ShiroUtils.sha256(userEntity.getPassword(), userEntity.getSalt()));

        return this.retBool(this.baseMapper.insert(userEntity));
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
            if (user != null) {
                return "该用户名已存在";
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
            if (user != null) {
                return "该手机号已注册";
            }
        }
        return null;
    }

}
