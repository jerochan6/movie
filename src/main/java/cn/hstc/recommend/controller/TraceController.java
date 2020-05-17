package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.TraceEntity;
import cn.hstc.recommend.service.TraceService;
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
@RequestMapping("trace")
public class TraceController {
    @Autowired
    private TraceService traceService;

    /**
     * 列表
     */
    @RequestMapping("/listPage")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = traceService.queryPage(params);

        return new Result().ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        TraceEntity trace = traceService.getById(id);

        return new Result<TraceEntity>().ok(trace);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody TraceEntity trace){
        traceService.save(trace);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TraceEntity trace){
        traceService.updateById(trace);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        traceService.removeByIds(Arrays.asList(ids));

        return new Result().ok("删除成功");
    }

}
