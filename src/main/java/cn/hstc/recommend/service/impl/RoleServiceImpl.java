package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.dao.UserRoleDao;
import cn.hstc.recommend.entity.UserRoleEntity;
import cn.hstc.recommend.utils.Constant;
import cn.hstc.recommend.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.RoleDao;
import cn.hstc.recommend.entity.RoleEntity;
import cn.hstc.recommend.service.RoleService;


@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {

    @Autowired
    UserRoleDao userRoleDao;

    private static List<Integer> allChildRolesIds = new ArrayList<Integer>();
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RoleEntity> page = this.page(
                new Query<RoleEntity>().getPage(params),
                new QueryWrapper<RoleEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<RoleEntity> list(){
        QueryWrapper<RoleEntity> wrapper = new QueryWrapper<RoleEntity>();
        if(Constant.currentId != Constant.SUPER_ADMIN){
            List<UserRoleEntity> userRoleEntities = userRoleDao.selectList(new QueryWrapper<UserRoleEntity>()
                .eq("user_id",Constant.currentId));
            List<Integer> roleIds = new ArrayList<>();
            for(UserRoleEntity userRoleEntity : userRoleEntities){
                roleIds.add(userRoleEntity.getRoleId());
            }
            if(userRoleEntities.size() == 0){
                return new ArrayList<>();
            }
            wrapper.in(roleIds.size()!=0,"r.id",roleIds);
            List<RoleEntity> roleEntities = this.baseMapper.selectAllRole(wrapper);
            //查询所有角色包括子角色
            Set<Integer> allRoleIds = new LinkedHashSet<>();
            for(RoleEntity roleEntity : roleEntities){
                allRoleIds.add(roleEntity.getId());
                getChildRoles(roleEntity.getId());
                if(allChildRolesIds.size() != 0){
                    for(int childRolesId : allChildRolesIds){
                        allRoleIds.add(childRolesId);
                    }
                }
                allChildRolesIds = new ArrayList<>();
            }

            List<RoleEntity> allRoles = this.baseMapper.selectAllRole(new QueryWrapper<RoleEntity>().in(allRoleIds.size()!=0,"r.id",allRoleIds));

            return allRoles;
        }

        return this.baseMapper.selectAllRole(new QueryWrapper());
    }

    @Override
    public boolean save(RoleEntity roleEntity){
        roleEntity.setCreateId(Constant.currentId);
        roleEntity.setCreateTime(new Date());
        return this.retBool(this.baseMapper.insert(roleEntity));
    }

    @Override
    public RoleEntity getById(Integer id){

        RoleEntity roleEntity = this.baseMapper.selectById(id);
        if(roleEntity.getParentId() == 0){
            roleEntity.setParentName("");
        }else{
            roleEntity.setParentName(this.baseMapper.selectById(roleEntity.getParentId()).getName());
        }


        return roleEntity;
    }

    public void getChildRoles(Integer roleId){
        List<RoleEntity> roleEntities = this.baseMapper.selectList(new QueryWrapper<RoleEntity>().eq("parent_id",roleId));
        if(roleEntities == null){
            return;
        }
        for(RoleEntity roleEntity : roleEntities){
            allChildRolesIds.add(roleEntity.getId());
            getChildRoles(roleEntity.getId());
        }
    }
}