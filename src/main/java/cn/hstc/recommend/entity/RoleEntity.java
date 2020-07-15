package cn.hstc.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-07-06 14:47:47
 */
@TableName("role")
public class RoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private Integer parentId;
	/**
	 * 
	 */
	private Integer createId;
	/**
	 * 
	 */
	private String remark;
	/**
	 * 
	 */
	private Date createTime;

	@TableField(exist = false)
	private List<Integer> menuIds;

	@TableField(exist = false)
	private String createName;

	@TableField(exist = false)
	private String parentName;

	public Integer getId() {
		return id;
	}

	public String getParentName() {
		return parentName;
	}

	@Override
	public String toString() {
		return "RoleEntity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", parentId=" + parentId +
				", createId=" + createId +
				", remark='" + remark + '\'' +
				", createTime=" + createTime +
				", createName='" + createName + '\'' +
				", parentName='" + parentName + '\'' +
				'}';
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String crreateName) {
		this.createName = crreateName;
	}

	public List<Integer> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Integer> menuIds) {
		this.menuIds = menuIds;
	}
}
