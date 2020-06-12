package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.utils.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.dao.UserDao;
import cn.hstc.recommend.entity.UserEntity;
import cn.hstc.recommend.service.UserService;
import org.springframework.transaction.annotation.Transactional;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private TokenHelp tokenHelp;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public Result loginValidate(String userName,String password) {
        //查找是否存在该手机号
        UserEntity userEntity = this.baseMapper.selectOne(new QueryWrapper<UserEntity>()
                .eq("user_name",userName));
       //若不存在，提示用户登录失败
        if(null == userEntity){
            return new Result().error("该账号未注册，请进行注册！");
        }
        //验证账号和密码是否正确
        if(userEntity.getUserName().equals(userName) &&
                userEntity.getPassword().equals(password)){

            String token = tokenHelp.getToken(userEntity);
            Result result = new Result<>();
            Map<String,Object> map  = new HashMap<String,Object>();
            map.put("token",token);
            map.put("user",userEntity);
            return result.ok(map);
        }
        return new Result().error("登录失败，请检查账号和密码！");
    }



}
