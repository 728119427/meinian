package com.atguigu.mytest;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UploadTest {

    @Test
    public void test1() throws FileNotFoundException {

        File file = new File("f://redis.png");
        upload(file,"image/png","redis.png");
    }


    public void upload(File file, String mimeType, String fileName) throws FileNotFoundException {



        try {
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(
                    "CEnvo7uzX5eCtplVo47O2X4VievOYUd30fyy5QXO3", "2IkOtPcGZB9GbuwvTZQr2Y40Pmuee1uFaBn0JPwX0riIGeG3eZhVZbP1e5wHNj4HSQ");
            ObjectConfig config = new ObjectConfig("cn-gd", "ufileos.com");
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(file, mimeType)
                    .nameAs(fileName)
                    .toBucket("java0223")
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(long bytesWritten, long contentLength) {

                        }
                    })
                    .execute();

            if (response != null || response.getRetCode() == 0) {
                String url = UfileClient.object(objectAuthorization, config)
                        .getDownloadUrlFromPrivateBucket(fileName, "java0223", 31536000).createUrl();
                System.out.println(url);
            } else {

            }
        } catch (UfileClientException e) {
            e.printStackTrace();
        } catch (UfileServerException e) {
            e.printStackTrace();

        }

    }
}
