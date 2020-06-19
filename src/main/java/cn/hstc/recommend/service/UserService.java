package cn.hstc.recommend.service;

import cn.hstc.recommend.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-08 15:00:40
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Result loginValidate(String userName,String password);

    @Override
    boolean updateById(UserEntity userEntity);

    @Override
    boolean save(UserEntity userEntity);
//   String getToken(UserEntity user);
}

