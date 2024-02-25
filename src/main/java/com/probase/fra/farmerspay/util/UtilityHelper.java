package com.probase.fra.farmerspay.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;
import java.util.stream.Collectors;

public class UtilityHelper {

    private final static Logger log = LoggerFactory.getLogger(UtilityHelper.class);
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");

    public static String generateBCryptPassword(String password)
    {
        String generatedSecuredPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        return generatedSecuredPasswordHash;
    }


    public static Object decryptData(String encryptedStr, String bankKey)
    {
        try{
            log.info("encryptedStr = " + encryptedStr);
            byte[] decode = org.apache.commons.codec.binary.Base64.decodeBase64(encryptedStr.getBytes("UTF-8"));
            String decodeStr = new String(decode, "UTF-8");
            log.info("Decoded = " + decodeStr);
            JSONObject jsonObject = new JSONObject(decodeStr);
            String iv1 = jsonObject.getString("iv");
            String value = jsonObject.getString("value");
            String mac = jsonObject.getString("mac");
            byte[] keyValue = bankKey.getBytes("UTF-8");
			/*String dec = decryptDataNew(keyValue, iv1, encryptedStr, mac);
			log.info("dec = " + dec);
			return dec;
		}catch(Exception e)
		 {
			 log.info(e.getMessage());
			 return null;
		 }*/
            log.info("value = " + value);

            log.info("bankKey = " + bankKey);
            Key key = new SecretKeySpec(keyValue, "AES");
            byte[] iv = org.apache.commons.codec.binary.Base64.decodeBase64(iv1.getBytes("UTF-8"));
            byte[] decodedValue = org.apache.commons.codec.binary.Base64.decodeBase64(value.getBytes("UTF-8"));


            log.info(key.getAlgorithm());
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding"); // or PKCS5Padding
            c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] decValue = c.doFinal(decodedValue);
            log.info(new String(decValue));

            int firstQuoteIndex = 0;
            byte b;
//            log.info(decValue[0]);
            if(decValue[0] == 105 || decValue[0] == 100 ){
                b = ((byte)';');
            }
            else{
                b = ((byte)'"');
            }

            log.info("length>--" + decValue.length);
            log.info("" + decValue[0]);

//			for(int j1=0; j1<decValue.length; j1++)
//				log.info("j1[" + j1 + "] == " + decValue[j1]);

            log.info("" + new String(decValue, "UTF-8"));
            while(firstQuoteIndex<decValue.length && decValue[firstQuoteIndex] != b){
//				log.info("firstQuoteIndex..." + firstQuoteIndex);
                firstQuoteIndex++;
            }
            String vl = "";


            if(decValue[0] == 105 || decValue[0] == 100){
                vl = new String(Arrays.copyOfRange(decValue, 2, decValue.length - 1), "UTF-8");
                log.info("vl..." + vl);
                if(decValue[0] == 105)
                {
                    return Integer.valueOf(vl);
                }else if(decValue[0] == 100)
                {
                    return Long.valueOf(vl);
                }
            }else{
                //vl = new String(Arrays.copyOfRange(decValue, firstQuoteIndex + 1, decValue.length-2), "UTF-8");
                log.info("vl1..." + vl);
                vl = new String(Arrays.copyOfRange(decValue, firstQuoteIndex + 1, decValue.length-2), "UTF-8");
                log.info("vl1..." + vl);
            }
            return (vl);
        }catch(Exception e)
        {
            log.info(e.getMessage());
            log.info(e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }



    public static String encryptData(Object toencrypt, String bankKey) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, JSONException, IllegalBlockSizeException, BadPaddingException {
        log.info("toEncrypt");
        log.info("{}", toencrypt);
        log.info("{}", bankKey);
        String strtoencrypt = "s:" + toencrypt.toString().length() + ":\"" + toencrypt.toString() + "\";";
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(bankKey.getBytes("UTF-8"), "AES");
        byte[] iv = RandomStringUtils.randomAlphanumeric(16).getBytes("UTF-8");
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(iv));
        byte[] df = cipher.doFinal(strtoencrypt.getBytes("UTF-8"));
        byte[] val = org.apache.commons.codec.binary.Base64.encodeBase64(df);
        byte[] iv1 = org.apache.commons.codec.binary.Base64.encodeBase64(iv);
        byte[] dest = new byte[iv1.length + val.length];
        System.arraycopy(iv, 0, dest, 0, iv.length);
        System.arraycopy(val, 0, dest, iv1.length, val.length);
        //cipher.
        key = new SecretKeySpec(bankKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        mac.update(iv1);
        mac.update(val);


        //Arrays.
        byte[] mc = (mac.doFinal());
        JSONObject  js = new JSONObject();
        js.put("iv", new String(iv1, "UTF-8"));
        js.put("value", new String(val, "UTF-8"));
        js.put("mac", new String(mc, "UTF-8"));
        log.info(js.toString());
        String encStr = new String(org.apache.commons.codec.binary.Base64.encodeBase64(js.toString().getBytes()), "UTF-8");
        return (encStr);
    }


    public AlgorithmParameterSpec getIV(Cipher cipher) {
        AlgorithmParameterSpec ivspec;
        byte[] iv = new byte[cipher.getBlockSize()];
        new SecureRandom().nextBytes(iv);
        ivspec = new IvParameterSpec(iv);
        return ivspec;
    }




}
