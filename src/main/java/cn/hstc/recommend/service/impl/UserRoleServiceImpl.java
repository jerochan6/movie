package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.entity.RoleEntity;
import cn.hstc.recommend.service.RoleService;
import cn.hstc.recommend.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.UserRoleDao;
import cn.hstc.recommend.entity.UserRoleEntity;
import cn.hstc.recommend.service.UserRoleService;


@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRoleEntity> implements UserRoleService {

    @Autowired
    private RoleService roleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserRoleEntity> page = this.page(
                new Query<UserRoleEntity>().getPage(params),
                new QueryWrapper<UserRoleEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<UserRoleEntity> getRolesByUser(Integer userId){
        QueryWrapper<UserRoleEntity> wrapper = new QueryWrapper<UserRoleEntity>();
            wrapper.eq("user_id",userId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<Integer> getRoleIdsByUserId(Integer userId){
        List<UserRoleEntity> roles = this.getRolesByUser(userId);

        List<Integer> roleIds = new ArrayList<>();
        for(UserRoleEntity userRoleEntity : roles){
            roleIds.add(userRoleEntity.getRoleId());
        }
        return roleIds;
    }

    @Override
    public List<String> getRoleNamesByUserId(Integer userId){
        List<UserRoleEntity> roles = this.getRolesByUser(userId);

        List<String> roleNames = new ArrayList<>();
        for(UserRoleEntity userRoleEntity : roles){
            if(userRoleEntity.getRoleId() != null){
                RoleEntity roleEntity = roleService.getById(userRoleEntity.getRoleId());
                if(roleEntity != null){
                    roleNames.add(roleEntity.getName());
                }
            }
        }
        return roleNames;
    }
}
