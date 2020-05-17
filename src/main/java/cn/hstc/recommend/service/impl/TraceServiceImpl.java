package cn.hstc.recommend.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.TraceDao;
import cn.hstc.recommend.entity.TraceEntity;
import cn.hstc.recommend.service.TraceService;


@Service("traceService")
public class TraceServiceImpl extends ServiceImpl<TraceDao, TraceEntity> implements TraceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TraceEntity> page = this.page(
                new Query<TraceEntity>().getPage(params),
                new QueryWrapper<TraceEntity>()
        );

        return new PageUtils(page);
    }

}
