package utils;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;

import java.io.File;
import java.io.FileNotFoundException;

public class UcloudUtils {

    public static String upload(File file, String mimeType, String fileName) throws FileNotFoundException {



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
                return url;
            } else {
                throw new RuntimeException("上传失败");
            }
        } catch (UfileClientException e) {
            e.printStackTrace();
        } catch (UfileServerException e) {
            e.printStackTrace();

        }
        return "";
    }
}
