package cn.hstc.recommend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.entity.RoleEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-07-06 14:47:47
 */
public interface RoleService extends IService<RoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    RoleEntity getById(Integer id);

    @Override
    boolean updateById(RoleEntity roleEntity);
}

