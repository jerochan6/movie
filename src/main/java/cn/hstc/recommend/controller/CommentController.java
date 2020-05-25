package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.CommentEntity;
import cn.hstc.recommend.service.CommentService;
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
@RequestMapping("comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 列表
     */
    @RequestMapping("/listPage")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = commentService.queryPage(params);

        return new Result().ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        CommentEntity comment = commentService.getById(id);

        return new Result<CommentEntity>().ok(comment);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody CommentEntity comment){
        commentService.save(comment);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody CommentEntity comment){
        commentService.updateById(comment);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        commentService.removeByIds(Arrays.asList(ids));

        return new Result().ok("删除成功");
    }

}
