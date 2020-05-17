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
 * @date 2020-05-17 10:02:22
 */
@TableName("recommend")
public class RecommendEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 推荐表id
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

	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}

	@Override
	public String toString() {
		return "RecommendEntity{" +
				"id=" + id +
				", userId=" + userId +
				", movieId=" + movieId +
				'}';
	}
}
