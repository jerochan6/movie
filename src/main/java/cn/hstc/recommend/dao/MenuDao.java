package cn.hstc.recommend.dao;

import cn.hstc.recommend.entity.MenuEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-07-06 14:47:47
 */
@Mapper
public interface MenuDao extends BaseMapper<MenuEntity> {

    List<String> queryMenuNames(@Param("ew") QueryWrapper wrapper);

    List<Integer> queryMenuIds(@Param("ew") QueryWrapper wrapper);
}
