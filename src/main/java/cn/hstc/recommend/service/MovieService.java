package cn.hstc.recommend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.entity.MovieEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-12 20:49:52
 */
public interface MovieService extends IService<MovieEntity> {

    PageUtils queryPage(Map<String, Object> params, QueryWrapper wrapper);

    MovieEntity getById(Integer id);

    boolean removeByIds(Collection<? extends Serializable> idList,String path);

    @Override
    boolean save(MovieEntity movieEntity);
}

