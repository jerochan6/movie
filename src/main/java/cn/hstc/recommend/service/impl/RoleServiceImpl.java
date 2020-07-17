package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.dao.RoleMenuDao;
import cn.hstc.recommend.dao.UserRoleDao;
import cn.hstc.recommend.entity.RoleMenuEntity;
import cn.hstc.recommend.entity.UserEntity;
import cn.hstc.recommend.entity.UserRoleEntity;
import cn.hstc.recommend.exception.RRException;
import cn.hstc.recommend.service.RoleMenuService;
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
import org.springframework.transaction.annotation.Transactional;


@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {

    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    ShiroUtils shiroUtils;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    private RoleMenuDao roleMenuDao;
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
        //如果登录账号为非超级管理员账号，则根据该账号拥有的角色查询
        if(Constant.currentId != Constant.SUPER_ADMIN){
            List<UserRoleEntity> userRoleEntities = userRoleDao.selectList(new QueryWrapper<UserRoleEntity>()
                .eq("user_id",Constant.currentId));
            //如果该账号没有角色，则直接返回空集合
            if(userRoleEntities.size() == 0){
                return new ArrayList<>();
            }
            //封装角色id集合，用于查询所有角色信息
            List<Integer> roleIds = new ArrayList<>();
            for(UserRoleEntity userRoleEntity : userRoleEntities){
                roleIds.add(userRoleEntity.getRoleId());
            }
            //查询所有角色信息集合
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
    @Transactional(rollbackFor = RRException.class)
    public boolean save(RoleEntity roleEntity){
        roleEntity.setCreateId(Constant.currentId);
        roleEntity.setCreateTime(new Date());
        for(Integer menuId : roleEntity.getMenuIds()){
            RoleMenuEntity roleMenuEntity = new RoleMenuEntity();
            roleMenuEntity.setRoleId(roleEntity.getId());
            roleMenuEntity.setMenuId(menuId);
            roleMenuDao.insert(roleMenuEntity);
        }
        return this.retBool(this.baseMapper.insert(roleEntity));
    }

    @Override
    public RoleEntity getById(Integer id){


        RoleEntity roleEntity = this.baseMapper.selectById(id);

        if(roleEntity != null){
            if(roleEntity.getParentId() == 0){
                roleEntity.setParentName("");
            }else{
                roleEntity.setParentName(this.baseMapper.selectById(roleEntity.getParentId()).getName());
            }

        }


        return roleEntity;
    }

    @Override
    @Transactional(rollbackFor = RRException.class)
    public boolean updateById(RoleEntity roleEntity){

        roleMenuService.removeById(roleEntity.getId());
        List<RoleMenuEntity> roleMenuEntities = new ArrayList<>();
        for(Integer menuId : roleEntity.getMenuIds()){
            RoleMenuEntity roleMenuEntity = new RoleMenuEntity();
            roleMenuEntity.setRoleId(roleEntity.getId());
            roleMenuEntity.setMenuId(menuId);
            roleMenuEntities.add(roleMenuEntity);
            roleMenuDao.insert(roleMenuEntity);
        }
        return true;
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
