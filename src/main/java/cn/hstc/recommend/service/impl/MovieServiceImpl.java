package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.entity.TagEntity;
import cn.hstc.recommend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
