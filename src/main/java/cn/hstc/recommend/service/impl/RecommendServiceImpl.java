package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.dao.MovieDao;
import cn.hstc.recommend.dao.OperateDao;
import cn.hstc.recommend.entity.MovieEntity;
import cn.hstc.recommend.entity.OperateEntity;
import cn.hstc.recommend.service.MovieService;
import cn.hstc.recommend.utils.Constant;
import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.jdbc.ConnectionPoolDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.RecommendDao;
import cn.hstc.recommend.entity.RecommendEntity;
import cn.hstc.recommend.service.RecommendService;

import javax.sql.DataSource;


@Service("recommendService")
public class RecommendServiceImpl extends ServiceImpl<RecommendDao, RecommendEntity> implements RecommendService {

    private DataSource dataSource;
    private OperateDao operateDao;
    private MovieService movieService;

    @Autowired
    RecommendServiceImpl(DataSource dataSource,OperateDao operateDao,MovieService movieService){
        this.dataSource = dataSource;
        this.operateDao = operateDao;
        this.movieService = movieService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        IPage<RecommendEntity> page = this.page(
                new Query<RecommendEntity>().getPage(params)
        );
        Integer maxNum = 10;
        if(params.get("maxNum") != null){
            maxNum = (Integer.parseInt(params.get("maxNum").toString()));
        }
        Set<RecommendEntity> set = this.generateRecommend(maxNum);
        Integer curPage = (int)page.getCurrent();
        Integer limit = ((int) page.getSize());
        page.setTotal(set.size());
        page.setRecords(new ArrayList<>(set));
        if(curPage != null && limit != null){
            ArrayList list = new ArrayList(set);
            LinkedList linkedList = new LinkedList();
            for(int i = (curPage-1) * limit;i < (curPage-1) * limit + limit && i < list.size();i ++){
                linkedList.add(list.get(i));
            }
            page.setRecords(linkedList);
        }

        return new PageUtils(page);
    }

    /**
     * @Author zehao
     * @Description //TODO 生成推荐电影的集合
     * @Date 10:15 2020/6/3/003
     * @Param []
     * @return java.util.List<cn.hstc.recommend.entity.RecommendEntity>
     **/
    @Override
    public Set<RecommendEntity> generateRecommend(int maxNum) {
        Set<RecommendEntity> recommendEntities =  new LinkedHashSet<>();
        List<RecommendedItem> recommendByContents = null;
        List<RecommendedItem> recommendByNeighborhoods = null;
        //查询用户是否在操作表中已经有数据
        List<OperateEntity> currentUserOperateEntities = operateDao.selectList(new QueryWrapper<OperateEntity>().eq("user_id",Constant.currentId));
        if(currentUserOperateEntities.size() > 0){
            //DataModel的建立
            DataModel model = new MySQLJDBCDataModel(
                    dataSource,"operate","user_id","movie_id","score","timestamp");
            //根据用户使用内容相似度推荐给用户
            recommendByContents = this.genericRecommenderByContent(model, Constant.currentId,maxNum/2);
            //根据计算邻居(对电影评分最多的用户)习惯推荐给用户
            recommendByNeighborhoods = this.genericRecommenderByNeighborhood(model,2, Constant.currentId,maxNum/2);

        }
        if (recommendByContents != null){
            for (RecommendedItem r : recommendByContents){
                RecommendEntity recommendEntity = new RecommendEntity();
                recommendEntity.setUserId(Constant.currentId);
                recommendEntity.setMovieId((int)r.getItemID());
                recommendEntities.add(recommendEntity);
            }
        }
        if (recommendByNeighborhoods != null) {
            for (RecommendedItem r : recommendByNeighborhoods){
                RecommendEntity recommendEntity = new RecommendEntity();
                recommendEntity.setUserId(Constant.currentId);
                recommendEntity.setMovieId((int)r.getItemID());
                recommendEntities.add(recommendEntity);
            }
        }
        //查询用户收藏或者想看的电影
        List<OperateEntity> currentUserPreferenceEntities =  operateDao.selectList(new QueryWrapper<OperateEntity>()
                .eq("user_id",Constant.currentId)
                .eq("will",1)
                .or()
                .eq("college",1)
        );
        //利用用户收藏或想看的电影推荐近似类型的电影
        if(currentUserPreferenceEntities.size() > 0){
            //   TreeMap: 能够把它保存的记录根据key排序,默认是按升序排序，也可以指定排序的比较器，
            //   当用Iterator 遍历TreeMap时，得到的记录是排过序的。TreeMap不允许key的值为null。非同步的。
            TreeMap<String,Integer> map = new TreeMap<>();
            for(OperateEntity operateEntity : currentUserPreferenceEntities){
                String t = movieService.getById(operateEntity.getMovieId()).getType();
                if(t != null){
                    String[] types = t.split(",");
                    for(String type : types){
                        if(!map.containsKey(type)){
                            map.put(type,1);
                        }else{
                            map.replace(type,map.get(type) + 1);
                        }
                    }
                }
            }
            Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            };
            // map转换成list进行排序
            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
            // 排序
            Collections.sort(list, valueComparator);
            if(list.size() > 0){
                List<MovieEntity> movieEntities = movieService.list(new QueryWrapper<MovieEntity>()
                        .apply("FIND_IN_SET("+list.get(0).getKey()+",type) ")
                );
                for(MovieEntity movieEntity : movieEntities){
                    //如果用户已将其设为想看系列，则不再推荐此电影
                    OperateEntity operateEntity = operateDao.selectOne(new QueryWrapper<OperateEntity>()
                        .eq("movie_id",movieEntity.getId())
                        .eq("user_id",Constant.currentId)
                        .eq("will",1)
                    );
                    if(operateEntity == null && recommendEntities.size() < maxNum){
                        RecommendEntity recommendEntity = new RecommendEntity();
                        recommendEntity.setUserId(Constant.currentId);
                        recommendEntity.setMovieId(movieEntity.getId());
                        recommendEntities.add(recommendEntity);
                    }
                }
            }
        }
        //不够推荐数，凑够推荐电影
        if(recommendEntities.size() < maxNum){
            //根据评分最高的推荐
            Map map = new HashMap();
            map.put("page",String.valueOf(1));
            map.put("limit",String.valueOf(maxNum - recommendEntities.size()));
            PageUtils recommendPageByScore = movieService.queryPage(
                    map,new QueryWrapper<MovieEntity>().orderByDesc("score")
            );
            List<MovieEntity> list = (List<MovieEntity>) recommendPageByScore.getList();
            for (MovieEntity movieEntity : list){
                RecommendEntity recommendEntity = new RecommendEntity();
                recommendEntity.setUserId(Constant.currentId);
                recommendEntity.setMovieId(movieEntity.getId());
                recommendEntities.add(recommendEntity);
            }
        }
        return recommendEntities;
    }

    /**
     * @Author zehao
     * @Description //TODO 根据用户相似度（内容）推荐算法
     * @Date 10:15 2020/6/3/003
     * @Param [model, userId, num]
     * @return java.util.List<org.apache.mahout.cf.taste.recommender.RecommendedItem>
     **/
    private  List<RecommendedItem>  genericRecommenderByContent( DataModel model,Integer userId,int num){
        List<RecommendedItem> recommendations = null;
        try{
            //计算内容相似度
            ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
//        UserNeighborhood neighborhood = new NearestNUserNeighborhood(2,similarity, model);//计算邻居
            // 创建推荐引擎
            Recommender recommender = new GenericItemBasedRecommender(model, similarity);
            //为用户1推荐2个
            recommendations = recommender.recommend(userId, num);
            for (RecommendedItem recommendation : recommendations) {
                System.out.println(recommendation);
            }
        }catch (TasteException e) {
            e.printStackTrace();
        }
        return recommendations;
    }
    /**
     * @Author zehao
     * @Description //TODO 根据用户相似度（邻居）推荐算法
     * @Date 10:13 2020/6/3/003
     * @Param [model, neighborhoodId, userId, num]
     * @return java.util.List<org.apache.mahout.cf.taste.recommender.RecommendedItem>
     **/
    private  List<RecommendedItem>  genericRecommenderByNeighborhood( DataModel model,Integer neighborhoodId,Integer userId,int num){
        List<RecommendedItem> recommendations = null;
        try{
            //计算相似度
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            //计算邻居
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighborhoodId,similarity, model);
            // 创建推荐引擎
            Recommender recommender = new GenericUserBasedRecommender(model,neighborhood, similarity);
            //为用户1推荐2个
            recommendations = recommender.recommend(userId, num);
            for (RecommendedItem recommendation : recommendations) {
                System.out.println(recommendation);
            }
        }catch (TasteException e) {
            e.printStackTrace();
        }
        return recommendations;
    }

    /**
     * @Author zehao
     * @Description //TODO 根据数据库的数据生成数据模型
     * @Date 10:16 2020/6/3/003
     * @Param []
     * @return org.apache.mahout.cf.taste.model.DataModel
     **/
    private DataModel getDataMode(){
        FastByIDMap<PreferenceArray> preferences = new FastByIDMap<PreferenceArray>();
            List<OperateEntity> operateEntities = operateDao.selectList(new QueryWrapper<OperateEntity>().groupBy("user_id"));
            for (OperateEntity operateEntity : operateEntities){
                List<OperateEntity> operateEntities2 = operateDao.selectList(
                        new QueryWrapper<OperateEntity>().eq("user_id",operateEntity.getUserId())
                );
                PreferenceArray preferenceArray = new GenericUserPreferenceArray(operateEntities2.size());
                preferenceArray.setUserID(0,operateEntity.getUserId());
                for(int i = 0;i < operateEntities2.size();i ++){
                    preferenceArray.setItemID(i,operateEntities2.get(i).getMovieId());
                    preferenceArray.setValue(i,operateEntities2.get(i).getScore().floatValue());
                }
                preferences.put(operateEntity.getUserId(),preferenceArray);
            }
            DataModel model = new GenericDataModel(preferences);
            return model;
    }
}
