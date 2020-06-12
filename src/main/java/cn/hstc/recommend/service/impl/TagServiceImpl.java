package cn.hstc.recommend.service.impl;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.utils.PageUtils;
import cn.hstc.recommend.utils.Query;
import cn.hstc.recommend.dao.TagDao;
import cn.hstc.recommend.entity.TagEntity;
import cn.hstc.recommend.service.TagService;


@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagDao, TagEntity> implements TagService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TagEntity> page = this.page(
                new Query<TagEntity>().getPage(params),
                new QueryWrapper<TagEntity>()
        );
        //遍历标签数组，插入父标签名
        List<TagEntity> tagList = page.getRecords();
        insertColumnName(tagList);
        return new PageUtils(page);
    }



    /**
     * @Author zehao
     * @Description //TODO 获取标签列表
     * @Date 16:01 2020/5/14/014
     * @Param
     * @return List<TagEntity></>
     **/
    @Override
    public List<TagEntity> getList(Map<String, Object> params) {
        //条件查询
        QueryWrapper wrapper = new QueryWrapper();
        if(params.get("parentId") != null){
            int parentId = Integer.parseInt(params.get("parentId").toString());
            wrapper.eq("parent_id",parentId);
        }
        if(params.get("tagName") != null){
            TagEntity tagEntity= this.baseMapper.selectOne(new QueryWrapper<TagEntity>().eq("tag_name",params.get("tagName").toString()));
            if(tagEntity != null){
                wrapper.eq("parent_id",tagEntity.getId());
            }
            if(tagEntity == null){
                return new ArrayList<>();
            }
        }
        List<TagEntity> list = this.baseMapper.selectList(wrapper);
        if(list != null){
            //遍历标签数组，插入父标签名
            insertColumnName(list);
        }

        return list;
    }
    /**
     * @Author zehao
     * @Description //TODO 删除节点时如果有子节点，则子节点也需删除
     * @Date 15:00 2020/5/16/016
     * @Param [tagEntity]
     * @return void
     **/
    public void removeChild(TagEntity tagEntity) {
        List<TagEntity> tagEntities = this.baseMapper.selectList(
                new QueryWrapper<TagEntity>().eq("parent_id",tagEntity.getId()));
        if(tagEntities != null){
            List<Integer> ids = new ArrayList<>();
            for (TagEntity t:
            tagEntities) {
                ids.add(t.getId());
            }
            this.baseMapper.deleteBatchIds(ids);
        }
    }

    private void insertColumnName(List<TagEntity> tagEntities){
        for (TagEntity tagEntity : tagEntities){
            //默认展开节点
            tagEntity.setOpen(true);
            //如果是根节点，插入空字符串
            if(tagEntity.getParentId() == 0){
                tagEntity.setParentName("");
            }
            else{
                //如果不是根节点，查出父节点的标签名
                String parentName = this.baseMapper.selectById(tagEntity.getParentId()).getTagName();
                //插入数据
                tagEntity.setParentName(parentName);
            }
        }
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable > idList){
      try {
          List<TagEntity> tagEntities = this.baseMapper.selectBatchIds(idList);
          this.baseMapper.deleteBatchIds(idList);
          for (TagEntity tagEntity :
                  tagEntities) {
              removeChild(tagEntity);
          }
          return true;
      }catch (Exception e){
          System.out.println(e);
          return false;
      }
    }
}
