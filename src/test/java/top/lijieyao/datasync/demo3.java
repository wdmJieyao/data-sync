package top.lijieyao.datasync;

import java.io.FileReader;
import java.util.Arrays;

/**
 * @Description:
 * @Author: LiJieYao
 * @Date: 2022/4/22 19:40
 */
public class demo3 {

    public static void main(String[] args) throws Exception{

        //补全代码...
        FileReader reader = new FileReader("C:\\Users\\39242\\Downloads\\order");
        char[] buff = new char[1024];
        StringBuilder builder = new StringBuilder();
        while (reader.read(buff) != -1){
            builder.append(buff);
        }

        String[] split = builder.toString().split("\0");
        System.out.println(Arrays.toString(split));
    }

}
