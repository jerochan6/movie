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
 * @date 2020-07-06 14:47:48
 */
@TableName("user_role")
public class UserRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer userId;
	/**
	 * 
	 */
	private Integer roleId;

	@Override
	public String toString() {
		return "UserRoleEntity{" +
				"userId=" + userId +
				", roleId=" + roleId +
				'}';
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}
