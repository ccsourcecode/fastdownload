package com.yomahub.fastdownload.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.output.FileWriterWithEncoding;

/**
* 读取文件工具类
*/
public class FileUtil {

    public final static String LINE_SEPARATOR = System.getProperty("line.separator");

    public static boolean createDirectory(File file) {
        if(file.exists()) {
            return true;
        }
        return file.mkdirs();
    }

    public static boolean createDirectory(String dirname) {
        return createDirectory(new File(dirname));
    }

    public static String readFile2String(String filename) throws IOException {
        return readFile2String(new File(filename));
    }

    public static String readFile2String(File file) throws IOException {
        if((file == null) || !file.exists() || file.isDirectory()) {
            return null;
        }
        return readInputStream2String(new FileInputStream(file));
    }

    public static String readInputStream2String(InputStream is)
            throws IOException {
        return readInputStream2String(is, "UTF-8");
    }

    public static String readInputStream2String(InputStream is, String charset)
            throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(is, charset));
            for(String str = null; (str = br.readLine()) != null; ) {
                sb.append(str).append(LINE_SEPARATOR);
            }
        } finally {
            closeIO(br);
        }
        return sb.toString().trim();
    }

    public static List<String> readFile2List(String filename)
            throws IOException {
        return readFile2List(new File(filename));
    }

    public static List<String> readFile2List(File file) throws IOException {
        if((file == null) || !file.exists() || file.isDirectory()) {
            return null;
        }
        BufferedReader br = null;
        List<String> list = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(file));
            for(String str = null; (str = br.readLine()) != null; ) {
                list.add(str);
            }
        } finally {
            closeIO(br);
        }
        return list;
    }

    public static void writeString2File(String str, String filename)
            throws IOException {
        writeString2File(str, new File(filename));
    }

    public static void writeString2File(String str, File file)
            throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriterWithEncoding(file, "UTF-8"));
            bw.write(str);
            bw.flush();
        } finally {
            closeIO(bw);
        }
    }

    public static void writeList2File(List<String> list, String filename)
            throws IOException {
        writeList2File(list, new File(filename), LINE_SEPARATOR);
    }

    public static void writeList2File(List<String> list, File file)
            throws IOException {
        writeList2File(list, file, LINE_SEPARATOR);
    }

    public static void writeList2File(List<String> list, String filename,
            String lineSeparator) throws IOException {
        writeList2File(list, new File(filename), lineSeparator);
    }

    public static void writeList2File(List<String> list, File file,
            String lineSeparator) throws IOException {
        StringBuffer sb = new StringBuffer();
        for(int i = 0, k = list.size(); i < k; i++) {
            if(i > 0) {
                sb.append(lineSeparator);
            }
            sb.append(list.get(i));
        }
        writeString2File(sb.toString(), file);
    }

    public static void closeIO(Closeable io) throws IOException {
        if(io != null) {
            io.close();
        }
    }

    public static void zipFile(File inFile, File outFile) throws IOException{
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
        ZipEntry ze = new ZipEntry(inFile.getName());
        zos.putNextEntry(ze);

        byte[] buf = new byte[2048];
        int readLen = 0;
        InputStream is = new BufferedInputStream(new FileInputStream(inFile));
        while ((readLen = is.read(buf, 0, 2048)) != -1) {
            zos.write(buf, 0, readLen);
        }
        is.close();

        zos.close();
    }

    public static void main(String[] args) throws Exception{
    	List list = FileUtil.readFile2List("c:/test/zyc.txt");
    	System.out.println(list);
    }
}
