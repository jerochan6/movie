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
        insertTypeName(movieEntities);
        return new PageUtils(page);
    }

    /**
     * @Author zehao
     * @Description 插入类型名
     * @Date 20:41 2020/5/15/015
     * @Param [movieEntities] 需要插入类型名的数组
     * @return void
     **/
    private void insertTypeName(List<MovieEntity> movieEntities){

        for(MovieEntity movieEntity: movieEntities){
            String type = movieEntity.getType();
            if(type != null &&  !type.trim().isEmpty()){
                String[] typeIds = type.split(",");
                StringBuffer tagName = new StringBuffer();
                for(String typeId : typeIds){
                    TagEntity tagEntity = tagService.getById(typeId);
                    if (tagEntity != null){
                        tagName.append(tagEntity.getTagName()+",");
                    }


                }
                if(tagName.length() != 0){
                    tagName.deleteCharAt(tagName.toString().length() - 1);
                }
                movieEntity.setTypeName(tagName.toString());
            }
        }

    }
}
