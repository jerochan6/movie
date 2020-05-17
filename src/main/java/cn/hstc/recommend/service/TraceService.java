package cn.hstc.recommend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.entity.TraceEntity;

import java.util.Map;

/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-17 10:02:22
 */
public interface TraceService extends IService<TraceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

