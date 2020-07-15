package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hstc.recommend.entity.MenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.RoleMenuEntity;
import cn.hstc.recommend.service.RoleMenuService;
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
@RequestMapping("rolemenu")
public class RoleMenuController {
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = roleMenuService.queryPage(params);

        return new Result().ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{roleId}")
    public Result info(@PathVariable("roleId") Integer roleId){
        List<MenuEntity> roleMenus = roleMenuService.getById(roleId);

        return new Result<List<MenuEntity>>().ok(roleMenus);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody RoleMenuEntity roleMenu){
        roleMenuService.save(roleMenu);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody RoleMenuEntity roleMenu){
        roleMenuService.updateById(roleMenu);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] roleIds){
        roleMenuService.removeByIds(Arrays.asList(roleIds));

        return new Result().ok("删除成功");
    }

}
