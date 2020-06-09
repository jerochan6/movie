package cn.hstc.recommend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.entity.RecommendEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-17 10:02:22
 */
public interface RecommendService extends IService<RecommendEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Set<RecommendEntity> generateRecommend(int maxNum);
}

