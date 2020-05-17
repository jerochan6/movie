package cn.hstc.recommend.service.impl;

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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OperateEntity> page = this.page(
                new Query<OperateEntity>().getPage(params),
                new QueryWrapper<OperateEntity>()
        );

        return new PageUtils(page);
    }

}
