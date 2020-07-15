package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.entity.UserRoleEntity;
import cn.hstc.recommend.service.UserRoleService;
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
import cn.hstc.recommend.dao.MenuDao;
import cn.hstc.recommend.entity.MenuEntity;
import cn.hstc.recommend.service.MenuService;


@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, MenuEntity> implements MenuService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private MenuService menuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MenuEntity> page = this.page(
                new Query<MenuEntity>().getPage(params),
                new QueryWrapper<MenuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<MenuEntity> getAllMenuByUser(Integer userId){
        if(userId == -1){
            userId = Constant.currentId;
        }
        UserRoleEntity userRoleEntity = userRoleService.getOne(new QueryWrapper<UserRoleEntity>()
            .eq("user_id",userId)
            .eq("role_id", Constant.SUPER_ADMIN)
        );
        if(userRoleEntity != null){
            return menuService.list();
        }
        return this.baseMapper.queryAllMenuByUser(userId);
    }

    @Override
    public List<MenuEntity> getMenuIdsByRoleId(Integer roleId){
        return this.baseMapper.queryMenuIdsByRoleId(roleId);
    }
}
