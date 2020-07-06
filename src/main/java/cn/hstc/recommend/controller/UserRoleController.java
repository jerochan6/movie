package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.UserRoleEntity;
import cn.hstc.recommend.service.UserRoleService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Result;



/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-07-06 14:47:48
 */
@RestController
@RequestMapping("userrole")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = userRoleService.queryPage(params);

        return new Result().ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{userId}")
    public Result info(@PathVariable("userId") Integer userId){
        UserRoleEntity userRole = userRoleService.getById(userId);

        return new Result<UserRoleEntity>().ok(userRole);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody UserRoleEntity userRole){
        userRoleService.save(userRole);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody UserRoleEntity userRole){
        userRoleService.updateById(userRole);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] userIds){
        userRoleService.removeByIds(Arrays.asList(userIds));

        return new Result().ok("删除成功");
    }

}
