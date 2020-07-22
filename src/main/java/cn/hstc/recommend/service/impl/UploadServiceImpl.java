package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.exception.RRException;
import cn.hstc.recommend.service.UploadService;
import cn.hstc.recommend.utils.UploadUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * @ClassName UploadServiceImpl
 * @Description TODO
 * @Author zehao
 * @Date 2020/7/21 20:38
 * @Version 1.0
 **/
@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Value("${fileserver.image.address}")
    private String IMAGE_SERVER_ADDRESS;
    @Value("${fileserver.image.ftpPort}")
    private String IMAGE_SERVER_PORT;
    @Value("${fileserver.image.rootPath}")
    private String IMAGE_SERVER_ROOTPATH;
    @Value("${fileserver.image.imagePath}")
    private String IMAGE_SERVER_IMAGEPATH;
    @Value("${fileserver.image.account}")
    private String IMAGE_SERVER_ACCOUNT;
    @Value("${fileserver.image.password}")
    private String IMAGE_SERVER_PASSWORD;

    @Override
    public boolean uploadImg(MultipartFile file,String newFileName) {

        boolean result = false;
        String IMAGE_SERVER_PATH = IMAGE_SERVER_ROOTPATH + IMAGE_SERVER_IMAGEPATH;
        if (file.isEmpty()) {
            throw new RRException("上传图片为空");
        }

        // 上传后的路径
        String filePath = IMAGE_SERVER_ADDRESS + IMAGE_SERVER_PORT + IMAGE_SERVER_PATH;

        //图片上传
        try {
            result = uploadFile(IMAGE_SERVER_ADDRESS, Integer.parseInt(IMAGE_SERVER_PORT), IMAGE_SERVER_ACCOUNT, IMAGE_SERVER_PASSWORD,
                    file.getInputStream(), IMAGE_SERVER_PATH, newFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean uploadFile(String ip, Integer port, String account, String password,
                              InputStream inputStream, String imageServerPath, String fileName) {
        boolean result = false;
        // 1. 创建一个FtpClient对象
        FTPClient ftpClient = new FTPClient();
        try {
            logger.info("开始登录ftp服务器 : " + ip + ":" + port);
            // 2. 创建 ftp 连接
            ftpClient.connect(ip, port);
            // 3. 登录 ftp 服务器
            ftpClient.login(account, password);
            // 获取连接ftp 状态返回值
            int reply = ftpClient.getReplyCode();
            logger.info("ftp回复码code : " + reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                // 如果返回状态不再 200 ~ 300 则认为连接失败
                ftpClient.disconnect();
                logger.info(account+" 连接失败 " );
                return result;
            }
            // 4. 读取本地文件
//          FileInputStream inputStream = new FileInputStream(new File("F:\\hello.png"));
            // 5. 设置上传的路径
            ftpClient.changeWorkingDirectory(imageServerPath);
            // 6. 修改上传文件的格式为二进制
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 7. 服务器存储文件，第一个参数是存储在服务器的文件名，第二个参数是文件流
            if (!ftpClient.storeFile(fileName, inputStream)) {
                logger.info(account+" 上传失败 " );
                return false;
            }
            logger.info("成功上传ftp服务器 : " + imageServerPath+fileName);
            // 8. 关闭连接
            inputStream.close();
            ftpClient.logout();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }

        return result;
    }
}
