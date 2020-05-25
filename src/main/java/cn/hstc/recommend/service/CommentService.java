package cn.hstc.recommend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.entity.CommentEntity;

import java.util.Map;

/**
 * 
 *
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-25 22:44:02
 */
public interface CommentService extends IService<CommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

