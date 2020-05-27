package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.entity.TagEntity;
import cn.hstc.recommend.service.TagService;
import cn.hstc.recommend.utils.UploadUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.MovieDao;
import cn.hstc.recommend.entity.MovieEntity;
import cn.hstc.recommend.service.MovieService;


@Service("movieService")
public class MovieServiceImpl extends ServiceImpl<MovieDao, MovieEntity> implements MovieService {

    @Autowired
    private TagService tagService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MovieEntity> page = this.page(
                new Query<MovieEntity>().getPage(params),
                new QueryWrapper<MovieEntity>()
        );
        List<MovieEntity> movieEntities = page.getRecords();
        insertColumnName(movieEntities);
        return new PageUtils(page);
    }

    /**
     * @Author zehao
     * @Description 插入标签id转换的标签名
     * @Date 20:41 2020/5/15/015
     * @Param [movieEntities] 需要插入类型名的数组
     * @return void
     **/
    private void insertColumnName(List<MovieEntity> movieEntities){

        for(MovieEntity movieEntity: movieEntities){
            String type = movieEntity.getType();
            String language = movieEntity.getLanguage();
            if(type != null &&  !type.trim().isEmpty()){
                
                movieEntity.setTypeName(getTagNames(type));
            }
            if(language != null &&  !language.trim().isEmpty()){
                movieEntity.setLanguageName(getTagNames(language));
            }
        }

    }
    /**
     * @Author zehao
     * @Description //TODO 根据标签id查询标签名
     * @Date 15:54 2020/5/17/017
     * @Param [ids]
     * @return java.lang.String
     **/
    private String getTagNames(String ids){
        String[] tagIds = ids.split(",");
        StringBuffer tagNames= new StringBuffer();
        for(String tagId : tagIds){
            TagEntity tagEntity = tagService.getById(tagId);
            if (tagEntity != null){
                tagNames.append(tagEntity.getTagName()+",");
            }
        }
        if(tagNames.length() != 0){
            tagNames.deleteCharAt(tagNames.toString().length() - 1);
        }
        return tagNames.toString();
    }
    /**
     * @Author zehao
     * @Description //TODO 根据多个电影id删除电影
     * @Date 9:49 2020/5/26/026
     * @Param [idList]
     * @return boolean
     **/
    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList,String path){
        //根据电影id获得电影
        List<MovieEntity> list = this.baseMapper.selectBatchIds(idList);
        //遍历电影集合，如果电影的图片不为空，则图片在本地中物理删除
        for (MovieEntity movie:
             list) {

            if(null != movie.getMovieImage()){
                // 上传后的路径
                String dirPath = path + UploadUtils.STATIC_PATH;
                //根据图片存储路径获取图片
                File image = new File(dirPath  + movie.getMovieImage());

                //如果图片存在，则删除
                if(image.exists()){
                    try{
                        image.delete();
                    }catch (Exception e){
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }

        //删除电影记录
        return SqlHelper.delBool(this.baseMapper.deleteBatchIds(idList));
    }
    /**
     * @Author zehao
     * @Description //TODO 保存电影信息
     * @Date 21:04 2020/5/26/026
     * @Param [movieEntity]
     * @return boolean
     **/
    @Override
    public boolean save(MovieEntity movieEntity){

        return this.retBool(this.baseMapper.insert(movieEntity));
    }
}
