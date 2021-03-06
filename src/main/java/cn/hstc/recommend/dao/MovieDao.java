package cn.hstc.recommend.dao;

import cn.hstc.recommend.entity.MovieEntity;
import cn.hstc.recommend.entity.OperateEntity;
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
 * @date 2020-05-12 20:49:52
 */
@Mapper
public interface MovieDao extends BaseMapper<MovieEntity> {

    List<MovieEntity> selectListPage(@Param("offset") long offset, @Param("size") long size, @Param("ew") Wrapper wrapper);
}
