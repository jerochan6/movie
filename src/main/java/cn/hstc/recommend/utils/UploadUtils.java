package cn.hstc.recommend.utils;

import java.io.File;
import java.util.UUID;

/**
 * @ClassName UploadUtils
 * @Description TODO
 * @Author zehao
 * @Date 2020/5/25/025 11:04
 * @Version 1.0
 **/
public class UploadUtils {

    public static String getUUIDName(String filename){
        // 获取后缀名
        String suffixName = filename.substring(filename.lastIndexOf("."));
        // 生成新文件名
        filename = UUID.randomUUID() + suffixName;
        return  filename;
    }
}
