package cn.hstc.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-12 20:49:52
 */
@TableName("movie")
public class MovieEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 电影id
	 */
	@TableId
	private Integer id;
	/**
	 * 电影简介
	 */
	private String movieInfo;
	/**
	 * 电影的上映时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date releaseTime;
	/**
	 * 导演名
	 */
	private String director;
	/**
	 * 编剧
	 */
	private String writer;
	/**
	 * 主演
	 */
	private String actor;
	/**
	 * 电影类型（多选id，用逗号,分开）
	 */
	private String type;
	/**
	 * 制片国家/地区
	 */
	private Integer sourceCountry;
	/**
	 * 语言
	 */
	private String language;
	/**
	 * 电影时长
	 */
	private Integer movieLength;
	/**
	 * 电影链接
	 */
	private String movieLink;
	/**
	 * 电影海报
	 */
	private String movieImage;
	/**
	 * 电影评分（平均）
	 */
	private Double score;
	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 * 电影
	 */
	private String movieName;

	//用于存放type中id的值
	@TableField(exist = false)
	private String typeName;
	//用于存放language中id的值
	@TableField(exist = false)
	private String languageName;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMovieInfo() {
		return movieInfo;
	}

	public void setMovieInfo(String movieInfo) {
		this.movieInfo = movieInfo;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSourceCountry() {
		return sourceCountry;
	}

	public void setSourceCountry(Integer sourceCountry) {
		this.sourceCountry = sourceCountry;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getMovieLength() {
		return movieLength;
	}

	public void setMovieLength(Integer movieLength) {
		this.movieLength = movieLength;
	}

	public String getMovieLink() {
		return movieLink;
	}

	public void setMovieLink(String movieLink) {
		this.movieLink = movieLink;
	}

	public String getMovieImage() {
		return movieImage;
	}

	public void setMovieImage(String movieImage) {
		this.movieImage = movieImage;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getLanguageName() {
		return languageName;
	}



	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	@Override
	public String toString() {
		return "MovieEntity{" +
				"id=" + id +
				", movieInfo='" + movieInfo + '\'' +
				", releaseTime=" + releaseTime +
				", director='" + director + '\'' +
				", writer='" + writer + '\'' +
				", actor='" + actor + '\'' +
				", type='" + type + '\'' +
				", sourceCountry=" + sourceCountry +
				", language='" + language + '\'' +
				", movieLength=" + movieLength +
				", movieLink='" + movieLink + '\'' +
				", movieImage='" + movieImage + '\'' +
				", score=" + score +
				", createTime=" + createTime +
				", movieName='" + movieName + '\'' +
				", typeName='" + typeName + '\'' +
				", languageName='" + languageName + '\'' +
				'}';
	}
}
