import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Test
 * @Description TODO
 * @Author zehao
 * @Date 2020/5/14/014 16:10
 * @Version 1.0
 **/
public class Test {

    public static void main(String[] args) throws IOException {
        File file = new File("/");
        String fileDirPath = new String(file.getCanonicalPath() +"/src/main/resources/" );

        File fileDir = new File(fileDirPath);
        System.out.println(fileDir.getAbsolutePath());
    }
}
