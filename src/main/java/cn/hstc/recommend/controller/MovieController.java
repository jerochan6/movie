package cn.hstc.recommend.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hstc.recommend.entity.MovieEntity;
import cn.hstc.recommend.service.MovieService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Result;



/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-12 20:49:52
 */
@RestController
@RequestMapping("movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    /**
     * 列表(页面)
     */
    @RequestMapping("/listPage")
    public Result listPage(@RequestParam Map<String, Object> params){
        PageUtils page = movieService.queryPage(params);

        return new Result().ok(page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        MovieEntity movie = movieService.getById(id);

        return new Result<MovieEntity>().ok(movie);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody MovieEntity movie){
        movieService.save(movie);

        return new Result().ok("保存成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody MovieEntity movie){
        if(movie.getType().isEmpty()){
            movie.setType(" ");
        }
        movieService.updateById(movie);
        
        return new Result().ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        movieService.removeByIds(Arrays.asList(ids));

        return new Result().ok("删除成功");
    }

}
