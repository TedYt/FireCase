package com.hytera.fcls.presenter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cctv on 2017/3/13.
 */

public class StreamTools {

    /**
     * 流的工具类
     */
        /**
         * 读取一个流 把流的内容转化成字符串
         * @param is
         * @return
         */
        public static String readStream(InputStream is) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while((len = is.read(buffer))!=-1){
                baos.write(buffer, 0, len);
            }
            is.close();
            return baos.toString();
        }
}
