package cn.hstc.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-14 14:31:34
 */
@TableName("tag")
public class TagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签id
	 */
	@TableId
	private Integer id;
	/**
	 * 父层id
	 */
	private Integer parentId;
	/**
	 * 标签名
	 */
	private String tagName;
	/**
	 * 注解声明不是表中的列，由联合查询生成
	 * 父标签名
	 **/
	@TableField(exist = false)
	private String parentName;

	//默认展开节点
	@TableField(exist = false)
	private Boolean open;

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Override
	public String toString() {
		return "TagEntity{" +
				"id=" + id +
				", parentId=" + parentId +
				", tagName='" + tagName + '\'' +
				", parentName='" + parentName + '\'' +
				'}';
	}
}
