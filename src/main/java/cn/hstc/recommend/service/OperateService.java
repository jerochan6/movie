package cn.hstc.recommend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.entity.OperateEntity;

import java.util.Map;

/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-17 10:02:22
 */
public interface OperateService extends IService<OperateEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

