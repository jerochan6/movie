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
 * @date 2020-05-25 22:44:02
 */
@TableName("operate")
public class OperateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 操作表id
	 */
	@TableId
	private Integer id;
	/**
	 * 用户id
	 */
	private Integer userId;
	/**
	 * 电影id
	 */
	private Integer movieId;
	/**
	 * 分数
	 */
	private Double score;
	/**
	 * 收藏（0：未收藏，1：已收藏）
	 */
	private Integer college;
	/**
	 * 想看（0：不想看，1：想看）
	 */
	private Integer will;

	@TableField(exist = false)
	private String userName;

	@TableField(exist = false)
	private String movieName;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMovieId() {
		return movieId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Integer getCollege() {
		return college;
	}

	public void setCollege(Integer college) {
		this.college = college;
	}

	public Integer getWill() {
		return will;
	}

	public void setWill(Integer will) {
		this.will = will;
	}

	@Override
	public String toString() {
		return "OperateEntity{" +
				"id=" + id +
				", userId=" + userId +
				", movieId=" + movieId +
				", score=" + score +
				", college=" + college +
				", will=" + will +
				", userName='" + userName + '\'' +
				", movieName='" + movieName + '\'' +
				'}';
	}
}
