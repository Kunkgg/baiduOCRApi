package com.nldy.uploader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;


/**
 * 文件合并的servlet
 * tesseract ocr
 *
 * @author 弄浪的鱼
 */
public class UploaderCheckServlet extends HttpServlet {

    public static String SERVER_PATH = MyConfiguration.getString("upload_path");
//    public static String SERVER_PATH = "/Users/shui/Desktop/upload";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        response.setContentType("text/html;charset=utf-8");

        if ("mergeChunks".equals(action)) {
            System.out.println("开始合并文件...");
            // 合并文件
            String fileMd5 = request.getParameter("fileMd5");
            String fileName = request.getParameter("fileName");

            // 读取目录里面的所有文件
            File f = new File(SERVER_PATH + "/" + fileMd5);
            File[] fileArray = f.listFiles(new FileFilter() {

                // 排除目录，只要文件
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        return false;
                    }
                    return true;
                }
            });

            // 转成集合，便于排序
            List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));

            // 从小到大排序
            Collections.sort(fileList, new Comparator<File>() {

                public int compare(File o1, File o2) {
                    if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2
                            .getName())) {
                        return -1;
                    }
                    return 1;
                }

            });

            File outputFile = new File(SERVER_PATH + "/" + fileName);

            // 创建文件
            outputFile.createNewFile();

            // 输出流
            FileChannel outChannel = new FileOutputStream(outputFile)
                    .getChannel();

            // 合并
            FileChannel inChannel;
            for (File file : fileList) {
                inChannel = new FileInputStream(file).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                inChannel.close();

                // 删除分片
                file.delete();
            }

            // 清除文件夹
            File tempFile = new File(SERVER_PATH + "/" + fileMd5);
            if (tempFile.isDirectory() && tempFile.exists()) {
                tempFile.delete();
            }

            // 关闭流
            outChannel.close();


        } else if ("checkChunk".equals(action)) {
            System.out.println("checkChunk...");
            String fileMd5 = request.getParameter("fileMd5");
            String chunk = request.getParameter("chunk");
            String chunkSize = request.getParameter("chunkSize");

            File checkFile = new File(SERVER_PATH + "/" + fileMd5 + "/" + chunk);

            response.setContentType("text/html;charset=utf-8");
            //检查文件是否存在，且大小是否一致
            if (checkFile.exists() && checkFile.length() == Integer.parseInt(chunkSize)) {
                response.getWriter().write("{\"ifExist\":1}");
            } else {
                response.getWriter().write("{\"ifExist\":0}");
            }

        } else if ("translate-tess".equals(action)) {

            // tesseract 翻译

            String fileName = request.getParameter("fileName");
            String words = TesseractOCR.tessractOCR(fileName);
//            String words = TesseractOCR.readText(fileName);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("{\"words\":\"" + words + "\"}");

        } else if ("translate-baidu".equals(action)) {

            // 百度翻译
            String fileName = request.getParameter("fileName");
            String result = BaiduOCR.BaiduOCR(SERVER_PATH + "/" + fileName);
            String words = json2String(result);

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("{\"words\":\"" + words + "\"}");

        }


    }

    private String json2String(String result) {
        String wordList = "";

        JSONObject jsonObject = new JSONObject(result);

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("words_result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject wordsResult = jsonArray.getJSONObject(i);
                String word = wordsResult.getString("words");
                wordList += word;
//                wordList += "//n";
            }
            return wordList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
