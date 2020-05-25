package cn.hstc.recommend.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import cn.hstc.recommend.utils.UploadUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;


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
        if(movie.getLanguage().isEmpty()){
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

    /**
     * @Author zehao
     * @Description //TODO 上传图片接口
     * @Date 10:26 2020/5/25/025
     * @Param imageFile
     * @return path
     **/
    @RequestMapping(value = "/imgUpload")
    public Result imgUpload(@RequestBody MultipartFile file,HttpServletRequest request){

        String parentDir = request.getServletContext().getRealPath("/");
        if (file.isEmpty()) {
           return new Result().error("上传图片为空");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 上传后的路径
        File filePath = UploadUtils.getImgDirFile(parentDir);
        // 生成新文件名
        fileName = UploadUtils.getUUIDName(fileName);
        File dest = new File(filePath + "/" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(filePath + "/" + fileName);
        return new Result().ok(UploadUtils.IMG_PATH_PREFIX+"/"+fileName);
    }
}
