/*
 * Copyright (c) 2020 Jeneral Samopal Company
 * Design and Programming by Alex Dovby
 */

package com.jsc.smarthome;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

class FileUtils {

    // =========================================================
    static void SaveFile(String filePath, String FileContent) {
        //Создание объекта файла.
        File file = Environment.getExternalStorageDirectory();
        File fhandle = new File(file.getAbsolutePath() + filePath);
        try {
            //Если нет директорий в пути, то они будут созданы:
            File parentFile = fhandle.getParentFile();
            if (parentFile != null) {
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
            }

            //Если файл существует, то он будет перезаписан:
            fhandle.createNewFile();
            FileOutputStream fOut = new FileOutputStream(fhandle);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(FileContent);
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Path " + filePath + ", " + e.toString());
        }
    }

    // =========================================================
    static String readFile(Activity main, String filePath) {
        String state = Environment.getExternalStorageState();
        StringBuilder textBuilder;
        if (!(state.equals(Environment.MEDIA_MOUNTED))) {
            Toast.makeText(main, R.string.msg_no_sd, Toast.LENGTH_LONG).show();
        } else {
            BufferedReader reader = null;
            try {
                File file = Environment.getExternalStorageDirectory();
                File textFile = new File(file.getAbsolutePath() + filePath);
                reader = new BufferedReader(new FileReader(textFile));
                textBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    textBuilder.append(line);
                    textBuilder.append("\n");
                }
                // System.out.println("trace | Path " + filePath + ", src : " + textBuilder);
                return textBuilder.toString();

            } catch (FileNotFoundException e) {
                // TODO: handle exception
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
