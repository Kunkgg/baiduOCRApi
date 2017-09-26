package com.nldy.uploader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 用 tesseract 进行字符识别
 * created by shui 2017/9/25
 */
public class TesseractOCR {

    // 和程序同一目录下
    public static String PARENT_PATH = "/Users/shui/Desktop/";
    public static String FALIL_PATH = PARENT_PATH + "upload/";
    public static String RESULT_PATH = PARENT_PATH + "result";

    public static String TessractOCR(String fileName) {

        Runtime runTime = Runtime.getRuntime();
        String filePath = FALIL_PATH + fileName;
        String cmd = "tesseract " + filePath + " " + " " + RESULT_PATH + fileName.split(".")[0] + " " + " " + "-l chi_sim" + " " + "-psm 6";

        try {
            runTime.exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String temp = null;
        String result = "";
        try {
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
            while ((temp = bufferedReader.readLine()) != null) {
                result += temp;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return result;
        }
    }

    /**
     * 读取 text 文件中的内容
     *
     *
     * @return
     */
    public static String readText(String fileName){

        String filePath = RESULT_PATH + fileName;
        File result = new File(filePath);


        return null;
    }

}
