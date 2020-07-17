package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hstc.recommend.dao.UserDao;
import cn.hstc.recommend.service.UserService;
import cn.hstc.recommend.utils.Constant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.MenuEntity;
import cn.hstc.recommend.service.MenuService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Result;



/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-07-06 14:47:47
 */
@RestController
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        if(Constant.currentId != Constant.SUPER_ADMIN){
            List<Integer> menuIds = userService.getAllPermsIds(Constant.currentId);
            if(menuIds == null){
                return new Result().ok(null);
            }
            List<MenuEntity> menuEntities = menuService.list(new QueryWrapper<MenuEntity>()
                    .in(menuIds != null,"id",menuIds));
            return new Result().ok(menuEntities);

        }
        List<MenuEntity> list = menuService.list();


        return new Result().ok(list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        MenuEntity menu = menuService.getById(id);

        return new Result<MenuEntity>().ok(menu);
    }

    /**
     * @Author zehao
     * @Description //TODO 根据用户查找该用户拥有的权限
     * @Date 15:26 2020/7/12
     * @Param
     * @return
     **/
    @RequestMapping("/getByUser/{userId}")
    public Result<List<MenuEntity>> getByUser(@PathVariable("userId") Integer userId){
        List<MenuEntity> menus = menuService.getAllMenuByUser(userId);

        return new Result<List<MenuEntity>>().ok(menus);
    }

    /**
     * @Author zehao
     * @Description //TODO 根据角色查找该角色拥有的权限
     * @Date 15:26 2020/7/12
     * @Param
     * @return
     **/
    @RequestMapping("/getByRole/{roleId}")
    public Result<List<MenuEntity>> getByRole(@PathVariable("roleId") Integer roleId){
        List<MenuEntity> menus = menuService.getMenuIdsByRoleId(roleId);

        return new Result<List<MenuEntity>>().ok(menus);
    }
    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody MenuEntity menu){
        menuService.save(menu);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody MenuEntity menu){
        menuService.updateById(menu);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        menuService.removeByIds(Arrays.asList(ids));

        return new Result().ok("删除成功");
    }

}
