package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.entity.CommentEntity;
import cn.hstc.recommend.entity.OperateEntity;
import cn.hstc.recommend.entity.TagEntity;
import cn.hstc.recommend.service.CommentService;
import cn.hstc.recommend.service.TagService;
import cn.hstc.recommend.utils.UploadUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.MovieDao;
import cn.hstc.recommend.entity.MovieEntity;
import cn.hstc.recommend.service.MovieService;
import org.springframework.transaction.annotation.Transactional;


@Service("movieService")
public class MovieServiceImpl extends ServiceImpl<MovieDao, MovieEntity> implements MovieService {

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MovieDao movieDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper wrapper) {

        wrapper.apply("1=1 ");
        //根据类型查电影
        if(params.get("type") != null){
            String type = (String) params.get("type");
             wrapper.apply("FIND_IN_SET("+type+",type) ");
        }
        //根据语言查询电影
        if(params.get("language") != null){
            String language = (String) params.get("language");
            wrapper.apply("FIND_IN_SET("+language+",language) ");
        }
        //根据地区查询电影
        if(params.get("sourceCountry") != null){
            String sourceCountry = (String) params.get("sourceCountry");
            wrapper.apply("FIND_IN_SET("+sourceCountry+",source_country) ");
        }
        //根据年份查询
        if(params.get("releaseTime") != null){
            String releaseTime = (String) params.get("releaseTime");
           wrapper.like("release_time",releaseTime);
        }

        //根据电影名模糊查询
        if(params.get("movieName") != null){
            String movieName = (String) params.get("movieName");
            wrapper.like("movie_name",movieName);
        }

        IPage<MovieEntity> page = new Query<MovieEntity>().getPage(params);
        page.setTotal(this.baseMapper.selectCount(wrapper));
        wrapper.groupBy("m.id");
        //排序
        if(params.get("orderBy") != null){
            String orderBy = (String) params.get("orderBy");
            if(orderBy.equals("releaseTime")){
                orderBy = "release_time";
            }
            wrapper.orderByDesc(orderBy);
        }
        page.setRecords(movieDao.selectListPage(page.offset(),page.getSize(),wrapper));
        List<MovieEntity> movieEntities = page.getRecords();
        insertColumnName(movieEntities);
//        //根据热度排序
//        if(params.get("orderBy") != null){
//            String orderBy = (String) params.get("orderBy");
//            if(orderBy.equals("hot")){
//                movieEntities.sort(new Comparator<MovieEntity>() {
//                    @Override
//                    public int compare(MovieEntity o1, MovieEntity o2) {
//                        //按评论数降序
//                        if(o1.getNumOfCommentUsers() > o2.getNumOfCommentUsers()){
//                            return -1;
//                        }
//                        if(o1.getNumOfCommentUsers().equals(o2.getNumOfCommentUsers())){
//                            return 0;
//                        }
//                        return 1;
//                    }
//                });
//            }
//        }
        return new PageUtils(page);
    }

    @Override
    public MovieEntity getById(Integer id) {
        List<MovieEntity> movieEntities = new ArrayList<>();
        MovieEntity movieEntity = this.baseMapper.selectById(id);
        movieEntities.add(movieEntity);
        insertColumnName(movieEntities);
        return movieEntities.get(0);
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
            if(movieEntity.getType() != null &&  !movieEntity.getType().trim().isEmpty()){
                
                movieEntity.setTypeName(getTagNames(movieEntity.getType()));
            }
            if(movieEntity.getLanguage() != null &&  !movieEntity.getLanguage().trim().isEmpty()){
                movieEntity.setLanguageName(getTagNames(movieEntity.getLanguage()));
            }
            if(movieEntity.getSourceCountry() != null){
                movieEntity.setCountryName(getTagNames(String.valueOf(movieEntity.getSourceCountry())));
            }
//            //插入电影的评论数（根据用户去重）
//            movieEntity.setNumOfCommentUsers(commentService.count(
//                    new QueryWrapper<CommentEntity>()
//                        .eq("movie_id",movieEntity.getId())
//                        .groupBy("user_id")
//            ));
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
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList,String path){
        //根据电影id获得电影
        List<MovieEntity> list = this.baseMapper.selectBatchIds(idList);
        //遍历电影集合，如果电影的图片不为空，则图片在本地中s物理删除
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
        movieEntity.setCreateTime(new Date());
        return this.retBool(this.baseMapper.insert(movieEntity));
    }
}
