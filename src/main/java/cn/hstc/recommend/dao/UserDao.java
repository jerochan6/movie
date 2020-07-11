package cn.hstc.recommend.dao;

import cn.hstc.recommend.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-08 15:00:40
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     */
    List<String> queryAllPerms(Integer userId);

    /**
     * @Author zehao
     * @Description //TODO 查询用户的所有权限名
     * @Date 10:36 2020/7/10
     * @Param [userId]
     * @return java.util.List<java.lang.String>
     **/
    List<String> queryAllPermsNames(Integer userId);
    
    /**
     * @Author zehao
     * @Description //TODO 查询用户的所有权限Id
     * @Date 10:37 2020/7/10
     * @Param [userId]
     * @return java.util.List<java.lang.Integer>
     **/
    List<Integer> queryAllPermsIds(Integer userId);
}
