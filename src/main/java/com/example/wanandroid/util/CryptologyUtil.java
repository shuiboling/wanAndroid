package com.example.wanandroid.util;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;


import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 版权所有：中国农业银行软件开发中心
 * 系统名称：DesEncode
 * 创建时间：2017/11/7
 * 作者：ZhangYan
 */
public class CryptologyUtil {

    public static void getKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            //要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String s = byteToHexString(b);
            System.out.println(s);
            System.out.println("十六进制密钥长度为"+s.length());
            System.out.println("二进制密钥的长度为"+s.length()*4);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("没有此算法。");
        }
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex=Integer.toHexString(bytes[i]);
            if(strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if(strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getToken(String userId){
        String key = "ujio993r9juo",iv="jo8u9hinbk";
        String base64Str = java.util.Base64.getEncoder().encodeToString(userId.getBytes());
        Log.d("zyy0",base64Str);
        byte[] cipherTextByte = SymmetricEncryptNoBase64( base64Str, key, iv);
        Log.d("zyy1",java.util.Base64.getEncoder().encodeToString(cipherTextByte));
        int []out = new int[cipherTextByte.length] ;
        for (int offset = 0 ; offset < cipherTextByte.length; offset++) {
            int num = cipherTextByte[offset];
            if(num < 0){
                num += 256;
            }
            num = num << 2;

            out[offset] = num;
        }
        Log.d("zyy2",java.util.Base64.getEncoder().encodeToString(intToByteArray(out)));
        String cipherText = aesEncrypt(false,null,intToByteArray(out));
        Log.d("zyy3 cipher",cipherText);
        Log.d("zyy4",java.util.Base64.getEncoder().encodeToString(cipherText.getBytes()));
        parseToken(cipherText);

        String result = toSHA(cipherText);

        return result;
    }

    public static byte[] intToByteArray(int[] out) {
        byte[] result = new byte[out.length*4];
        for(int i = 0,j=0;j<out.length;i=i+4,j++){
            int num = out[j];
            result[i] = (byte)((num >> 24) & 0xFF);
            result[i+1] = (byte)((num >> 16) & 0xFF);
            result[i+2] = (byte)((num >> 8) & 0xFF);
            result[i+3] = (byte)(num & 0xFF);
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void parseToken(String result){
        try {
            byte[] d1 = decodeAes(result);
//            byte []out = new byte[d1.length] ;
            int[] nums = byteArrayToInt(d1);
            byte[] cipherByte = new byte[nums.length];
            for (int i = 0 ; i < nums.length; i++) {
                int num = nums[i];

                num = num >> 2;
                if(num > 127){
                    num = num - 256;
                }

                cipherByte[i] = (byte) num;
            }
            String key = "ujio993r9juo",iv="jo8u9hinbk";
            byte[] cipherTextByte = SymmetricDecryptNoBase64( cipherByte, key, iv);
            byte[] text = java.util.Base64.getDecoder().decode(cipherTextByte);
            String plain = new String(text);
            Log.d("zyy plain",plain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int[] byteArrayToInt(byte[] bytes) {
        int[] out = new int[bytes.length/4];
        for (int i = 0,k=0;i<bytes.length;i=i+4,k++){
            int value=0;
            for(int j = 0; j < 4; j++) {
                int shift= (3-j) * 8;
                value +=(bytes[i+j] & 0xFF) << shift;
            }
            out[k] = value;
        }
        return out;

    }

    public static byte[] decodeAes(String content) throws Exception {
        String key = "9d4r1eFtk30vc1bj";
        byte[] arr = string2ByteArr(content);
        byte[] raw = key.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(arr);
        return original;
    }

    public static byte[] string2ByteArr(String str) {
        byte[] bytes;
        bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2),16);
        }
        return bytes;
    }

    private static boolean validate(String signStr) {
        int i = signStr.length();
        String signature = signStr.substring(signStr.length() - 24,signStr.length());
        String basicText = signStr.substring(0,signStr.length() - 24);
        String hashBasicText = MD5_Hash(basicText);
        int j = hashBasicText.length();
        if(!signature.equals(hashBasicText)){
            return false;
        }else {
            return true;
        }
    }

    public static String Decrypt(String cipherText, String key, String iv){
        byte[] decodeBase64Str = Base64.decode(cipherText.getBytes(),android.util.Base64.DEFAULT);
        return new String(SymmetricDecryptNoBase64(decodeBase64Str,key,iv));
    }

    public  static byte[] SymmetricDecryptNoBase64(byte[] cipherTextByte, String key, String defaultIV) {
//        byte[] cipherTextByte = null;
        try {
//            cipherTextByte = cipherText.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7padding");
            //创建一个 DESKeySpec 对象，使用 8 个字节的key作为 DES 密钥的密钥内容。
            String legalKey = getLegalKey(key);
            DESKeySpec desKeySpec = new DESKeySpec(legalKey.getBytes());
            //返回转换指定算法的秘密密钥的 SecretKeyFactory 对象。
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //根据提供的密钥生成 SecretKey 对象。
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            //使用 iv 中的字节作为 IV 来构造一个 IvParameterSpec 对象。复制该缓冲区的内容来防止后续修改。
            String legalIV = getLegalIV(defaultIV);
            IvParameterSpec iv = new IvParameterSpec(legalIV.getBytes());
            //用密钥和一组算法参数初始化此 Cipher;Cipher：加密、解密、密钥包装或密钥解包，具体取决于 opmode 的值。
            cipher.init(Cipher.DECRYPT_MODE,secretKey,iv);
            return cipher.doFinal(cipherTextByte);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String MD5_Hash(String basicText){
        byte[] basicTextByte = null;
        try {
            basicTextByte = basicText.getBytes("UTF-8");
            MessageDigest md5= MessageDigest.getInstance("MD5");
            md5.update( basicTextByte);
            basicTextByte = md5.digest();
            int []out = new int[basicTextByte.length] ;
            int i;
            for (int offset = 0; offset < basicTextByte.length; offset++) {
                i = basicTextByte[offset];
                if (i < 0)
                    i += 256;
                out[offset] = i;
            }
            String s = String.valueOf(toBase64(out));
            return String.valueOf(toBase64(out));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String GenerateToken(String basicText, String key, String iv) {
        String str = SignAndEncrypt(basicText, key,iv);
        String encodeStr = Base64.encodeToString(str.getBytes(),android.util.Base64.DEFAULT).replaceAll("\n","");
        return encodeStr;
    }

    public static String SignAndEncrypt(String basicText, String key, String iv){
        String combinedText = basicText;
        String signature = MD5_Hash(combinedText);
        String fullText = combinedText + signature;
        return SymmetricEncrypt( fullText, key, iv);
    }

    public static String SymmetricEncrypt(String basicText, String key, String iv) {
        byte[] cipherTextByte = SymmetricEncryptNoBase64( basicText, key, iv);
        return  android.util.Base64.encodeToString(cipherTextByte, android.util.Base64.DEFAULT);
    }

    public  static byte[] SymmetricEncryptNoBase64(String basicText, String key, String defaultIV) {
        byte[] basicTextByte = null;
        try {
            basicTextByte = basicText.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7padding");
            //创建一个 DESKeySpec 对象，使用 8 个字节的key作为 DES 密钥的密钥内容。
            String legalKey = getLegalKey(key);
            DESKeySpec desKeySpec = new DESKeySpec(legalKey.getBytes());
            //返回转换指定算法的秘密密钥的 SecretKeyFactory 对象。
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //根据提供的密钥生成 SecretKey 对象。
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            //使用 iv 中的字节作为 IV 来构造一个 IvParameterSpec 对象。复制该缓冲区的内容来防止后续修改。
            String legalIV = getLegalIV(defaultIV);
            IvParameterSpec iv = new IvParameterSpec(legalIV.getBytes());
            //用密钥和一组算法参数初始化此 Cipher;Cipher：加密、解密、密钥包装或密钥解包，具体取决于 opmode 的值。
            cipher.init(Cipher.ENCRYPT_MODE,secretKey,iv);
            return cipher.doFinal(basicText.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String padLeft(int length, Object number) {
        String f = "%0" + length + "x";
        return String.format(f, number);
    }

    //AES加密
    public static String aesEncrypt(boolean isDebug, Context context, byte[] byteContent) {
        String key = "9d4r1eFtk30vc1bj";
        Cipher cipher = null;// 创建密码器
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密,
//            return byteArrayToHexStr(result);//16进制String
            String tmpString = String.format("%x",new BigInteger(1, result));
            int n = tmpString.length()/16+(tmpString.length()%16==0?0:1);
            return padLeft(16*n,new BigInteger(1, result));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKeySpec getSecretKey(final String password) {
            return new SecretKeySpec(password.getBytes(), "AES");// 转换为AES专用密钥
    }

    public static String getLegalIV(String iv) {
        String sTemp = iv;
        int ivLength = 8;
        if (sTemp.length() > ivLength) {
            sTemp = sTemp.substring(0, ivLength);
        }
        else if (sTemp.length() < ivLength) {
            sTemp = padRight(sTemp,ivLength, ' ');
        }

        return sTemp;
    }

    //byte[]转16进制
    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null){
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String getLegalKey(String key) {
        String sTemp = key;
        int keyLength = 8;
        if (sTemp.length()> keyLength) {
            sTemp = sTemp.substring(0, keyLength);
        }
        else if (sTemp.length()< keyLength) {
            sTemp = padRight(sTemp,keyLength, ' ');
        }

        return sTemp;
    }

    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    static private final int SIXTEENBIT = 16;
    static private final int EIGHTBIT = 8;
    static private final char PAD = '=';

    public static String toBase64(int[] data) throws UnsupportedEncodingException {
        if (data.length < 0)
            return "";
        int[] text = data;
        char[] base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();// 加密
        int lengthDataBits = text.length * 8;
        int fewerThan24bits = lengthDataBits % 24;// 加密字符串长度是否超过24
        int numberTriplets = lengthDataBits / 24;
        int number = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;// 计算字符串加密后字符总个数
        char[] toBase64Text = new char[number * 4];// 用来保存结果
        int s1, s2, s3;
        int index = 0, order = 0;
        for (int i = 0; i < numberTriplets; i++) {
            s1 = text[index++];
            s2 = text[index++];
            s3 = text[index++];
            toBase64Text[order++] = base[(s1 & 0xFC) >> 2];// 第一个6位
            toBase64Text[order++] = base[((s1 & 0x03) << 4) + ((s2 & 0xF0) >> 4)];// 第二个6位
            toBase64Text[order++] = base[((s2 & 0x0F) << 2) + ((s3 & 0xC0) >> 6)];// 第三个6位
            toBase64Text[order++] = base[s3 & 0x3f];// 第四个6位
        }
        /**
         * 一个字节的情况：将这一个字节的8个二进制位最后一组除了前面加二个0以外，后面再加4个0。这样得到一个二位的Base64编码，
         * 再在末尾补上两个"="号。
         */
        if (fewerThan24bits == EIGHTBIT) {
            int last = text[index++];
            toBase64Text[order++] = base[(last & 0xFC) >> 2];
            toBase64Text[order++] = base[((last & 0x03) << 4)];
            toBase64Text[order++] = PAD;
            toBase64Text[order++] = PAD;
        }
        /**
         * 二个字节的情况：将这二个字节的一共16个二进制位,转成三组，最后一组除了前面加两个0以外，后面也要加两个0。
         * 这样得到一个三位的Base64编码，再在末尾补上一个"="号。
         */
        if (fewerThan24bits == SIXTEENBIT) {
            s1 = text[index++];
            s2 = text[index++];
            toBase64Text[order++] = base[(s1 & 0xFC) >> 2];
            toBase64Text[order++] = base[(s1 & 0x03) << 4 + ((s2 & 0xF0) >> 4)];
            toBase64Text[order++] = base[(s2 & 0x0f) << 2];
            toBase64Text[order++] = PAD;
        }
        return new String(toBase64Text);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String toSHA(String content){
        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-256");
            byte[] shaBytes = md.digest(content.getBytes());

            String tmpString = String.format("%x",new BigInteger(1, shaBytes));
            int n = tmpString.length()/16+(tmpString.length()%16==0?0:1);
            String str = padLeft(16*n,new BigInteger(1, shaBytes));
            Log.d("zyy",java.util.Base64.getEncoder().encodeToString(shaBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "error";
    }

    //获取签名
    public static String getSign(boolean isDebug, Context context, String method, String queryString, String bodyString){
        String salt = "";
        byte[] basicTextByte = null;
        MessageDigest md5= null;
        try {
            basicTextByte = (method+"\n"+queryString+"\n"+bodyString+"\n"+salt).getBytes();
            md5 = MessageDigest.getInstance("SHA-256");
            md5.update( basicTextByte);
            basicTextByte = md5.digest();

            String tmpString = String.format("%x",new BigInteger(1, basicTextByte));
            int n = tmpString.length()/16+(tmpString.length()%16==0?0:1);
            return padLeft(16*n,new BigInteger(1, basicTextByte));
//            return String.format("%064x",new BigInteger(1, basicTextByte));
//            return new BigInteger(1, basicTextByte).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "error";
    }
}

