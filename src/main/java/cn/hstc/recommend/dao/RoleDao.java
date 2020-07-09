package cn.hstc.recommend.dao;

import cn.hstc.recommend.entity.RoleEntity;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-07-06 14:47:47
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {

    List<RoleEntity> selectAllRole(@Param("ew") Wrapper wrapper);
}