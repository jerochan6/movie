package cn.hstc.recommend.dao;

import cn.hstc.recommend.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-08 15:00:40
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
	
}
