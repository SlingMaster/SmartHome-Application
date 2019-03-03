/*
 * Copyright (c) 2018. Jeneral Samopal Company
 * Design and Programming by Alex Dovby
 */

package com.jsc.smarthome;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    // =========================================================
    public static void writeFileSD(String sd_path, String file_name, String data) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            System.out.println("SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();


        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + sd_path);
        // создаем каталог
        if (!sdPath.exists()) {
            sdPath.mkdir();
        }

        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, file_name);
        // System.out.println("sdFile : " + sdFile);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write(data);
            // закрываем поток
            bw.close();
            // System.out.println("Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    public static String readFileSD(String sd_path, String file_name) {
        // проверяем доступность SD
        String str = "";
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            System.out.println("SD-карта не доступна: " + Environment.getExternalStorageState());
            return str;
        }

        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + sd_path);
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, file_name);
        // System.out.println("Read file: " +  sdFile);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                // System.out.println("log json : " + str);
                return str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }
}
