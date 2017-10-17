package com.yunxin.service.natrpc.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by hedingwei on 11/10/2017.
 */
//ZipUtil
public class DataUtils {

    public static byte[] compress(String str) throws Exception {
        if (str == null || str.length() == 0) {
            return new byte[]{};
        }
        ByteArrayOutputStream obj=new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str.getBytes("UTF-8"));
        gzip.close();
        return obj.toByteArray();
    }

    public static byte[] compress(byte[] str) throws Exception {
        if (str == null || str.length == 0) {
            return new byte[]{};
        }
        ByteArrayOutputStream obj=new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str);
        gzip.close();
        return obj.toByteArray();
    }

    public static byte[] decompressData(byte[] str) throws IOException {
        if (str == null || str.length == 0) {
            return new byte[]{};
        }
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        IOUtils.copy(gis,bos);

        return bos.toByteArray();
    }

    public static String decompress(byte[] str) throws Exception {
        if (str == null || str.length == 0) {
            return "";
        }
//        System.out.println("Input String length : " + str.length);
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
            outStr += line;
        }
//        System.out.println("Output String lenght : " + outStr.length());
        return outStr;
    }

    public static String encode(String str) throws Exception {

        byte[] compressedData = compress(str);
        str = Base64.encodeBase64String(compressedData);

        byte[] encrypted = Base64.decodeBase64(EncryptUtil.encrypt(EncryptUtil.key, EncryptUtil.initVector,str));

        byte[] compressed = compress(encrypted);
        return Base64.encodeBase64String(compressed);


    }

    public static String decode(String str) throws Exception {
        byte[] data = Base64.decodeBase64(str);
        data = decompressData(data);
        String ss = EncryptUtil.decrypt(EncryptUtil.key, EncryptUtil.initVector,Base64.encodeBase64String(data));
        data = Base64.decodeBase64(ss);
        data = decompressData(data);
        ss = new String(data,"UTF-8");
        return ss;
    }

    public static void main(String[] args) throws Exception {
        String data = "Java is a high-level programming language originally developed by Sun Microsystems and released in 1995. Java runs on a variety of platforms, such as Windows, Mac OS, and the various versions of UNIX. This tutorial gives a complete understanding of Java.\n" +
                "\n" +
                "This reference will take you through simple and practical approaches while learning Java Programming language.\n" +
                "\n" +
                "Audience\n" +
                "This tutorial has been prepared for the beginners to help them understand the basic to advanced concepts related to Java Programming language.";
//        StringBuilder sb = new StringBuilder();
//        for(int i=0;i<10000;i++){
//            sb.append("aaa你好");
//        }
//        data = sb.toString();
        String encoded = DataUtils.encode(data);
        System.out.println("传输数据大小："+encoded.getBytes().length);
        String decoded = DataUtils.decode(encoded);
        System.out.println(decoded);
    }



}