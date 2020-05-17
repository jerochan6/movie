package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.Map;

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
 * @date 2020-05-17 10:02:22
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
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = operateService.queryPage(params);

        return new Result().ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        OperateEntity operate = operateService.getById(id);

        return new Result<OperateEntity>().ok(operate);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody OperateEntity operate){
        operateService.save(operate);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody OperateEntity operate){
        operateService.updateById(operate);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        operateService.removeByIds(Arrays.asList(ids));

        return new Result().ok("删除成功");
    }

}
