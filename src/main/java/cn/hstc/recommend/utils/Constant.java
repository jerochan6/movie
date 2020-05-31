package cn.hstc.recommend.utils;

/**
 * @ClassName Constant
 * @Description TODO
 * @Author zehao
 * @Date 2020/5/7/007 16:21
 * @Version 1.0
 **/

import java.util.LinkedHashMap;

/**
 * 常量
 *
 * @author Zero.
 */
public class Constant {
    /** 当前登录的用户Id **/
    public static Integer currentId;
    /** 超级管理员ID */
    public static final int SUPER_ADMIN = 1;
    /** 超级管理员账号名 */
    public static final String SUPER_ADMIN_NAME = "admin";
    /** 数据权限过滤 */
    public static final String SQL_FILTER = "sql_filter";
    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     *  升序
     */
    public static final String ASC = "asc";
    /**
     *  降序
     */
    public static final String DESC = "desc";

    /**
     * 手机号码正则
     **/
    public static final String MOBILEPHONEREX = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])\\d{8}$";
    /**
     * 座机号码正则
     **/
    public static final String HOMEPHONEREX = "^(\\d{2,4}-?)?\\d{7,8}$";
    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }



    /**
     * mybatisplus  忽略监测注解使用
     */
    public enum FieldStrategy {
        /**
         * 忽略判断
         */
        IGNORED(0),

        /**
         * 非NULL判断
         */
        NOT_NULL(1),
        /**
         * 非空判断
         */
        NOT_EMPTY(2);
        private int value;

        FieldStrategy(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


}
