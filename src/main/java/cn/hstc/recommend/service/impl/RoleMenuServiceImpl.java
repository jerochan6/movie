package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.entity.MenuEntity;
import cn.hstc.recommend.service.MenuService;
import cn.hstc.recommend.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.RoleMenuDao;
import cn.hstc.recommend.entity.RoleMenuEntity;
import cn.hstc.recommend.service.RoleMenuService;


@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenuEntity> implements RoleMenuService {

    @Autowired
    MenuService menuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RoleMenuEntity> page = this.page(
                new Query<RoleMenuEntity>().getPage(params),
                new QueryWrapper<RoleMenuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<MenuEntity> getById(Integer roleId) {
        if(roleId == Constant.SUPER_ADMIN){
            return menuService.list();
        }

        return menuService.getMenuIdsByRoleId(roleId);
    }

}
