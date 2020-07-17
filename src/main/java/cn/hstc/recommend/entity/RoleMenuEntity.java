package cn.hstc.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-07-06 14:47:47
 */
@TableName("role_menu")
public class RoleMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer roleId;
	/**
	 * 
	 */
	@TableId
	private Integer menuId;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getMenuId() {
		return menuId;
	}


	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	@Override
	public String toString() {
		return "RoleMenuEntity{" +
				"roleId=" + roleId +
				", menuId=" + menuId +
				'}';
	}

}
