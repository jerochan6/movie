package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.TagEntity;
import cn.hstc.recommend.service.TagService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Result;



/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-14 14:31:34
 */
@Api("标签页面")
@RestController
@RequestMapping("tag")
public class TagController {

    private TagService tagService;

    @Autowired
    TagController(TagService tagService){
        this.tagService = tagService;
    }
    /**
     * 页面列表
     */

    @RequestMapping("/listPage")
    public Result listPage(@RequestParam Map<String, Object> params){
        PageUtils page = tagService.queryPage(params);

        return new Result().ok(page);
    }
    /**
     * 数据列表
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        List<TagEntity> list = tagService.getList(params);

        return new Result().ok(list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        TagEntity tag = tagService.getById(id);

        return new Result<TagEntity>().ok(tag);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody TagEntity tag){
        tagService.save(tag);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TagEntity tag){
        tagService.updateById(tag);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        tagService.removeByIds(Arrays.asList(ids));

        return new Result().ok("删除成功");
    }

}
