package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.RoleEntity;
import cn.hstc.recommend.service.RoleService;
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
@RequestMapping("role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        List<RoleEntity> roleEntities = roleService.list();

        return new Result().ok(roleEntities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        RoleEntity role = roleService.getById(id);

        return new Result<RoleEntity>().ok(role);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody RoleEntity role){
        roleService.save(role);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody RoleEntity role){
        roleService.updateById(role);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        roleService.removeByIds(Arrays.asList(ids));

        return new Result().ok("删除成功");
    }

}
