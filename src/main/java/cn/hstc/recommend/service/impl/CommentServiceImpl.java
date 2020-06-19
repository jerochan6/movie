package cn.hstc.recommend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.CommentDao;
import cn.hstc.recommend.entity.CommentEntity;
import cn.hstc.recommend.service.CommentService;


@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        //设置查询条件
        QueryWrapper<CommentEntity> wrapper = new QueryWrapper<CommentEntity>();

        //关联多表查询，一次查询完成，提高效率
        IPage<CommentEntity> page = new Query<CommentEntity>().getPage(params);
        if(null != params.get("movieId")){
            Integer movieId = Integer.parseInt(((String) params.get("movieId")));
            wrapper.eq("movie_id",movieId);
        }
        if(null != params.get("userId")){
            Integer userId = Integer.parseInt(((String) params.get("userId")));
            wrapper.eq("user_id",userId);
        }

        page.setTotal(this.baseMapper.selectCount(wrapper));
        page.setRecords(commentDao.selectListPage(page.offset(),page.getSize(),wrapper));
        return new PageUtils(page);
    }

}
