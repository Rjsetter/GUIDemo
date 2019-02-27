package cn.yq.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
        // 判断文件是否存在
        public static void judeFileExists(File file) {

            if (file.exists()) {
                System.out.println("file exists");
            } else {
                System.out.println("file not exists, create it ...");
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        // 判断文件夹是否存在
        public static void judeDirExists(File file) {

            if (file.exists()) {
                if (file.isDirectory()) {
                    logger.info("JIRA文件夹已经存在，无须创建。");
                } else {
                    logger.info("the same name file exists, can not create dir");
                }
            } else {
                logger.info("JIRA文件夹不存在，现在创建它。");
                file.mkdir();
            }
        }
    public static void main(String[] args) {

        File file = new File("d:\\test_file.txt");
        FileUtil.judeFileExists(file);

        File dir = new File("d:\\test_dir");
        FileUtil.judeDirExists(dir);
    }
    }

