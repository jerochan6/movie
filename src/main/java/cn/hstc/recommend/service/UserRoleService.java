package cn.hstc.recommend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.entity.UserRoleEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-07-06 14:47:48
 */
public interface UserRoleService extends IService<UserRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<UserRoleEntity> getRolesByUser(Integer userId);

    List<Integer> getRoleIdsByUserId(Integer userId);

    List<String> getRoleNamesByUserId(Integer userId);
}

