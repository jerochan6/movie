package cn.hstc.recommend.utils;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description TODO
 * @Author zehao
 * @Date 2020/4/14/014 21:53
 * @Version 1.0
 **/
public class Result<T> implements Serializable{
        private static final long serialVersionUID = 1L;
        //成功码
        private int code = 0;
        private String msg = "success";
        private T data;

        public Result() {
        }

        public Result<T> ok(T data) {
            this.setData(data);
            return this;
        }

        public boolean success() {
            return this.code == 0;
        }

        public Result<T> error() {
            this.code = 500;
            this.msg = "";
            return this;
        }

        public Result<T> error(int code) {
            this.code = code;
            this.msg = "";
            return this;
        }

        public Result<T> error(int code, String msg) {
            this.code = code;
            this.msg = msg;
            return this;
        }

        public Result<T> error(String msg) {
            this.code = 500;
            this.msg = msg;
            return this;
        }

        public int getCode() {
            return this.code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return this.msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public T getData() {
            return this.data == null ? (T)new JSONArray() : this.data;
        }

        public void setData(T data) {
            this.data = data;
        }


}
