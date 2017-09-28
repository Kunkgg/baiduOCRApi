package com.nldy.uploader;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 用 tesseract 进行字符识别
 * created by shui 2017/9/25
 */
public class TesseractOCR {

    // 和程序同一目录下
    public static String FALIL_PATH = MyConfiguration.getString("upload_path");
    public static String RESULT_PATH = MyConfiguration.getString("result_path");

    public static String tessractOCR(String fileName) {

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
    public static String readText(String fileName) {

        String filePath = RESULT_PATH + fileName.split("\\.")[0] + ".txt";
        String text = " ";
        String line = " ";

        try {
            File result = new File(filePath);
            result.exists();
            result.length();

            System.out.println(result.exists()+"");
            System.out.println(result.length()+"");

            // 建立一个输入流对象reader
            InputStreamReader reader  = new InputStreamReader(
                    new FileInputStream(result), "utf-8");
            BufferedReader br = new BufferedReader(reader);
            while ((line=br.readLine()) != null){
                text += line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public boolean exists(String fileName) {
        String filePath = RESULT_PATH + fileName.split("\\.")[0] + ".txt";
        File file = new File(filePath);
        return file.exists();
    }

    public boolean rename(String fileName) {
        String filePath = RESULT_PATH + fileName.split("\\.")[0] + ".txt";
        File file = new File(filePath);
        return file.renameTo(file);
    }

    public boolean isLock(String fileName) throws IOException {
        String filePath = RESULT_PATH + fileName.split("\\.")[0] + ".txt";

        RandomAccessFile fis = new RandomAccessFile(filePath + ".lock", "rw");
        FileChannel lockfc = fis.getChannel();
        FileLock flock = lockfc.tryLock();
        if (flock == null) {
            System.out.println("程序正在运行...");
            return true;
        }

        return false;
    }

    public static boolean isFileClosed(String fileName) {

        String filePath = RESULT_PATH + fileName.split("\\.")[0] + ".txt";
        File file = new File(filePath);
        try {
            Process plsof = new ProcessBuilder(new String[]{"lsof", "|", "grep", file.getAbsolutePath()}).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(plsof.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(file.getAbsolutePath())) {
                    reader.close();
                    plsof.destroy();
                    return false;
                }
            }
            reader.close();
            plsof.destroy();
            return true;
        } catch (Exception ex) {
            // TODO: handle exception ...
            return false;
        }

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
