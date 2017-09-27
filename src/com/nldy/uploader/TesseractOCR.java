package com.nldy.uploader;

import java.io.*;

/**
 * 用 tesseract 进行字符识别
 * created by shui 2017/9/25
 */
public class TesseractOCR {

    // 和程序同一目录下
    public static String PARENT_PATH = "/Users/shui/Desktop/";
    public static String FALIL_PATH = PARENT_PATH + "upload/";
    public static String RESULT_PATH = PARENT_PATH + "result/";

    public synchronized String tessractOCR(String fileName) {

        Runtime runTime = Runtime.getRuntime();
        String filePath = FALIL_PATH + fileName;
        String cmd = "tesseract " + filePath + " " + " " + RESULT_PATH + fileName.split("\\.")[0] + " " + " " + "-l chi_sim" + " " + "-psm 6";

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
     * @return
     */
    public synchronized String readText(String fileName) {

        String filePath = RESULT_PATH + fileName.split("\\.")[0] + ".txt";
        String text = " ";
        String line = " ";

        try {
            File result = new File(filePath);
            InputStreamReader reader = null; // 建立一个输入流对象reader
            reader = new InputStreamReader(
                    new FileInputStream(result),"utf-8");
                BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            line = br.readLine();
            do {
                text += line;
                line = br.readLine(); // 一次读入一行数据
            } while (line != null);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public static void main(String[] args) {

//        String fileName = "aa.jpg";
//        String[] a = fileName.split("\\.");
//        System.out.println(a);
//        String aaa = fileName.split("\\.")[0];
//        System.out.println(aaa);

//        String result = ReadText("aa.txt");
//        System.out.println(result);
    }

}
