package com.nldy.uploader;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * created by shui 2017/9/19
 */
public class BaiduOCR {

    // 服务器路径
    public static String SERVER_PATH = MyConfiguration.getString("upload_path");

    //设置APPID/AK/SK
    public static final String APP_ID = MyConfiguration.getString("app_id");
    public static final String API_KEY = MyConfiguration.getString("api_key");
    public static final String SECRET_KEY = MyConfiguration.getString("secret_key");

    public static String BaiduOCR(String filePath) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 调用接口
//        String path = "/Users/shui/Desktop/bb.png";
//        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
//        System.out.println(res.toString(2));


        return generalRecognition(client,filePath);
    }

    public static String generalRecognition(AipOcr client,String filePath) {
        // 自定义参数定义
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "false");
        options.put("language_type", "CHN_ENG");

        // 参数为本地图片路径
//        String imagePath = "/Users/shui/Desktop/2.png";
//        String imagePath = filePath;
//        JSONObject response1 = client.basicGeneral(imagePath, options);
//        String resString = response1.toString();
//        System.out.println(resString);
//        return response1.toString();
//        // 参数为本地图片文件二进制数组
        byte[] file = readImageFile(filePath);
        if(file != null){
            JSONObject response2 = client.basicGeneral(file, options);
//            System.out.println(response2.toString());
            return response2.toString();
        }
        return null;
//
        // 参数为图片url
//        String url = "http://some.com/a.jpg";
//        JSONObject response3 = client.basicGeneralUrl(url, options);
//        System.out.println(response3.toString());
    }

    private static byte[] readImageFile(String filePath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
