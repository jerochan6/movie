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



    // 项目根路径下的目录  -- SpringBoot static 目录相当于是根路径下（SpringBoot 默认）
    public final static String IMG_PATH_PREFIX = "upload/imgs";

    public final static String STATIC_PATH="WEB-INF/classes/static/";
    public static File getImgDirFile(String path){
        File file = new File("");
        // 构建上传文件的存放 "文件夹" 路径
        String fileDirPath = new String(path + STATIC_PATH + IMG_PATH_PREFIX);

        File fileDir = new File(fileDirPath);
        if(!fileDir.exists()){
            // 递归生成文件夹
            fileDir.mkdirs();
        }
        return fileDir;
    }

    public static String getUUIDName(String filename){
        // 获取后缀名
        String suffixName = filename.substring(filename.lastIndexOf("."));
        // 生成新文件名
        filename = UUID.randomUUID() + suffixName;
        return  filename;
    }
}
