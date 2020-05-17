package cn.hstc.recommend.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.RecommendDao;
import cn.hstc.recommend.entity.RecommendEntity;
import cn.hstc.recommend.service.RecommendService;


@Service("recommendService")
public class RecommendServiceImpl extends ServiceImpl<RecommendDao, RecommendEntity> implements RecommendService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RecommendEntity> page = this.page(
                new Query<RecommendEntity>().getPage(params),
                new QueryWrapper<RecommendEntity>()
        );

        return new PageUtils(page);
    }

}
