package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.Map;

import cn.hstc.recommend.interceptor.PassToken;
import cn.hstc.recommend.interceptor.UserAdminToken;
import cn.hstc.recommend.interceptor.UserLoginToken;
import cn.hstc.recommend.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.UserEntity;
import cn.hstc.recommend.service.UserService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Result;



/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-08 15:00:40
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户列表,需要管理员登录后查看
     */
    @UserAdminToken
    @RequestMapping("/listPage")
    public Result listPage(@RequestParam Map<String, Object> params){

        PageUtils page = userService.queryPage(params);
        return new Result().ok(page);
    }


    /**
     * 信息，需要用户登录后查看
     */
    @UserLoginToken
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        //用户只可以查看自己的信息，管理员可以查看所有用户的信息
        if(Constant.currentId != Constant.SUPER_ADMIN){
            UserEntity user = userService.getById(Constant.currentId);
            return new Result<UserEntity>().ok(user);
        }
        UserEntity user = userService.getById(id);
        return new Result<UserEntity>().ok(user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody UserEntity user){
        userService.save(user);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @UserLoginToken
    @RequestMapping("/update")
    public Result update(@RequestBody UserEntity user){

        userService.updateById(user);
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @UserAdminToken
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        userService.removeByIds(Arrays.asList(ids));

        return new Result().ok("删除成功");
    }

    /**
     * 登录
     */
    @PassToken
    @RequestMapping("/login")
    public Result login(@RequestParam String userName,String password){
        return  userService.loginValidate(userName,password);
    }

//    /**
//     * 验证用户是否登录
//     */
//    @UserLoginToken
//    @RequestMapping("/isLogin")
//    public boolean isLogin(){
//        return  true;
//    }
}
