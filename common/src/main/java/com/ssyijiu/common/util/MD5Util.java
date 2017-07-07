package com.ssyijiu.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ssyijiu on 2016/8/16.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */
public class MD5Util {

    private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
        'b', 'c', 'd', 'e', 'f' };


    private MD5Util() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("MD5Util cannot be instantiated !");
    }


    /**
     * MD5加密
     *
     * @param data 明文
     * @return 密文
     */
    public static String getMD5(String data) {
        return getMD5(data.getBytes());
    }


    /**
     * MD5加密
     *
     * @param data 明文数组
     * @return 密文
     */
    public static String getMD5(byte[] data) {
        byte[] digest;
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md.update(data);

            digest = md.digest();

        } catch (NoSuchAlgorithmException neverHappened) {
            throw new RuntimeException(neverHappened);
        }
        // 把密文转换成十六进制的字符串形式
        return bytes2Hex(digest);
    }


    /**
     * MD5加密,获取后10密文
     *
     * @param data 明文
     * @return 后10位密文
     */
    public static String getMD5_10(String data) {
        return getMD5_10(data.getBytes());
    }


    /**
     * MD5加密,获取后10密文
     *
     * @param data 明文数组
     * @return 后10位密文
     */
    public static String getMD5_10(byte[] data) {
        byte[] digest;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            digest = md.digest();
        } catch (NoSuchAlgorithmException neverHappened) {
            throw new RuntimeException(neverHappened);
        }
        return bytes2Hex(digest).substring(22);
    }


    /**
     * 获取一个文件的MD5校验码
     *
     * @param path 文件路径
     * @return MD5检验码
     */
    public static String getMD5File(String path) {

        File file = new File(path);

        FileInputStream in = null;
        FileChannel ch = null;
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            md.update(byteBuffer);
            digest = md.digest();
        } catch (NoSuchAlgorithmException neverHappened) {
            throw new RuntimeException(neverHappened);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
            if (ch != null) {
                try {
                    ch.close();
                } catch (IOException ignored) {
                }
            }
        }

        return bytes2Hex(digest);
    }


    /**
     * 一个byte转为2个hex字符
     */
    private static String bytes2Hex(byte[] src) {
        char[] res = new char[src.length * 2];
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }
        return new String(res);
    }
}
