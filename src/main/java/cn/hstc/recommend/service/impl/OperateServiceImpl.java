package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.dao.MovieDao;
import cn.hstc.recommend.entity.MovieEntity;
import cn.hstc.recommend.utils.Constant;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.OperateDao;
import cn.hstc.recommend.entity.OperateEntity;
import cn.hstc.recommend.service.OperateService;


@Service("operateService")
public class OperateServiceImpl extends ServiceImpl<OperateDao, OperateEntity> implements OperateService {

    private OperateDao operateDao;
    private MovieDao movieDao;

    @Autowired
    OperateServiceImpl(OperateDao operateDao,MovieDao movieDao){
        this.operateDao = operateDao;
        this.movieDao = movieDao;
    }
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //设置查询条件
        QueryWrapper<OperateEntity> wrapper = new QueryWrapper<OperateEntity>();
        //关联多表查询，一次查询完成，提高效率
        IPage<OperateEntity> page = new Query<OperateEntity>().getPage(params);
        page.setTotal(this.baseMapper.selectCount(wrapper));
        page.setRecords(operateDao.selectListPage(page.offset(),page.getSize(),wrapper));
        return new PageUtils(page);
    }
    /**
     * @Author zehao
     * @Description //TODO 修改用户操作信息
     * @Date 21:09 2020/5/26/026
     * @Param [operateEntity]
     * @return boolean
     **/
    @Override
    public boolean updateById(OperateEntity operateEntity){
        boolean flag = true;
        operateEntity.setUserId(Constant.currentId);
        QueryWrapper<OperateEntity> wrapper = new QueryWrapper<OperateEntity>()
                .eq("user_id",operateEntity.getUserId())
                .eq("movie_id",operateEntity.getMovieId());
        OperateEntity operate = this.baseMapper.selectOne(wrapper);
        if(operate == null){
            flag =  SqlHelper.retBool(this.baseMapper.insert(operateEntity));
        }else {
            operateEntity.setId(operate.getId());
            flag = SqlHelper.retBool(this.baseMapper.updateById(operateEntity));
        }
        if (operateEntity.getScore() != null){
            Double newScore = operateDao.selectScoreByMovie(operateEntity.getMovieId());
            MovieEntity movieEntity = new MovieEntity();
            movieEntity.setId(operateEntity.getMovieId());
            movieEntity.setScore(newScore);
            flag = SqlHelper.retBool(movieDao.updateById(movieEntity));
        }

        return flag;
    }

}
