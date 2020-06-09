package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.Map;

import cn.hstc.recommend.interceptor.UserLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.RecommendEntity;
import cn.hstc.recommend.service.RecommendService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Result;



/**
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-17 10:02:22
 */
@RestController
@RequestMapping("recommend")
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    /**
     * 列表
     */
    @UserLoginToken
    @RequestMapping("/listPage")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = recommendService.queryPage(params);
        return new Result().ok(page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        RecommendEntity recommend = recommendService.getById(id);

        return new Result<RecommendEntity>().ok(recommend);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody RecommendEntity recommend){
        recommendService.save(recommend);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody RecommendEntity recommend){
        recommendService.updateById(recommend);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        recommendService.removeByIds(Arrays.asList(ids));

        return new Result().ok("删除成功");
    }

    /**
     * 删除
     */
    @UserLoginToken
    @RequestMapping("/get")
    public boolean get(){
        recommendService.generateRecommend(10);

        return true;
    }

}
