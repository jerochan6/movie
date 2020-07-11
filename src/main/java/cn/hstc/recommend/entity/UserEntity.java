package cn.hstc.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Zero
 * @email 570057386@qq.com
 * @date 2020-05-08 15:00:40
 */
@TableName("user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     * 登录用户名
     */
    private String userName;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 随机字符
     **/
    private String salt;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 用户性别
     */
    private String sex;
    /**
     * 用户年龄
     */
    private Integer age;
    /**
     * 用户学历
     */
    private String educational;
    /**
     * 用户职业
     */
    private String job;
    /**
     * 个性签名
     */
    private String remark;
    /**
     * 头像
     */
    private String image;

    /**
     * 权限id集合
     **/
    @TableField(exist = false)
    private Collection<Integer> menuIds;
    /**
     * 权限集合
     **/
    @TableField(exist = false)
    private Collection<String> menuNames;

    /**
     * 角色Id集合
     **/
    @TableField(exist = false)
    private Collection<Integer> roleIds;

    /**
     * 角色名集合
     **/
    @TableField(exist = false)
    private Collection<String> roleNames;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEducational() {
        return educational;
    }

    public void setEducational(String educational) {
        this.educational = educational;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Collection<Integer> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(Collection<Integer> menuIds) {
        this.menuIds = menuIds;
    }

    public Collection<String> getMenuNames() {
        return menuNames;
    }

    public void setMenuNames(Collection<String> menuNames) {
        this.menuNames = menuNames;
    }

    public Collection<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Collection<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public Collection<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(Collection<String> roleNames) {
        this.roleNames = roleNames;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", phone='" + phone + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", educational='" + educational + '\'' +
                ", job='" + job + '\'' +
                ", remark='" + remark + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
