package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.Map;

import cn.hstc.recommend.interceptor.UserAdminToken;
import cn.hstc.recommend.interceptor.UserLoginToken;
import cn.hstc.recommend.utils.Constant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.OperateEntity;
import cn.hstc.recommend.service.OperateService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Result;



/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-25 22:44:02
 */
@RestController
@RequestMapping("operate")
public class OperateController {
    @Autowired
    private OperateService operateService;

    /**
     * 列表
     */

    @RequestMapping("/listPage")
    public Result listPage(@RequestParam Map<String, Object> params){
        PageUtils page = operateService.queryPage(params);

        return new Result().ok(page);
    }


    /**
     * 信息
     */

    @UserLoginToken
    @RequestMapping("/info/{movieId}")
    public Result info(@PathVariable("movieId") Integer movieId){

        OperateEntity operate = operateService.getOne(
                new QueryWrapper<OperateEntity>()
                        .eq("user_id", Constant.currentId)
                        .eq("movie_id",movieId)
        );
        if(operate == null){
            operate = new OperateEntity();
        }
        return new Result<OperateEntity>().ok(operate);
    }

//    /**
//     * 保存
//     */
//    @UserLoginToken
//    @RequestMapping("/save")
//    public Result save(@RequestBody OperateEntity operate){
//        operateService.save(operate);
//
//        return new Result().ok("保存成功");
//    }

    /**
     * 修改
     */
    @UserLoginToken
    @RequestMapping("/update")
    public Result update(@RequestBody OperateEntity operate){

        operateService.updateById(operate);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @UserLoginToken
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        operateService.removeByIds(Arrays.asList(ids));

        return new Result().ok("删除成功");
    }

}
