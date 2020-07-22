package cn.hstc.recommend.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    /**
     * @Author zehao
     * @Description //TODO 上传一张图片
     * @Date 20:37 2020/7/21
     * @Param [file]
     * @return java.lang.String 上传成功后保存的路径
     **/
    boolean uploadImg( MultipartFile file,String newFileName);

}
