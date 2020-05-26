package cn.hstc.recommend.service.impl;

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

    @Autowired
    private OperateDao operateDao;

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

}
