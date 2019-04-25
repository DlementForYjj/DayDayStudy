package Dlement.util.password;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


/**
 * 签名工具类
 * Description: 签名工具类  
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		榕基党员E家项目组
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年5月30日 下午4:16:54		LinLW			1.0				1.0 Version
 */
public class SignTool {

	private static final String ENCODING = "UTF-8";
	private static final String BLOWFISH = "BLOWFISH";
	private static final String SHA1 = "SHA-1";
	/**
	 * 对某个文本或其他多媒体字节流进行签名
	 * 这里使用流模式，可以支持大数据量签名。而不会大量降低性能。
	 * 
	 * @param source InputStream 文本或其他多媒体字节流。
	 * @param key String 秘钥
	 * @return String 前面结果
	 * @author 榕基党员E家项目组
	 */
	public static String sign(InputStream is, String key) {

		try {
			//不管文章有多长，先做个SHA-1的数字摘要
			MessageDigest sha1 = null;
			sha1 = MessageDigest.getInstance(SHA1); // MD5 SHA-1
			sha1.reset();
			byte[] buff=new byte[8192];
			int read=-1;
			while((read=is.read(buff))>0){
				sha1.update(buff,0,read);
			}
		    byte[] digestRet= sha1.digest();
			
		    //然后使用各自的秘钥签名，这里选用BLOWFISH 主要是因为BLOWFISH可以随意设置秘钥
			Cipher cipherBlowfish= Cipher.getInstance(BLOWFISH);
			SecretKeySpec keySpec=null;
			try {
				keySpec = new SecretKeySpec(key.getBytes("ENCODING"), BLOWFISH);
			} catch (UnsupportedEncodingException e) {
				keySpec = new SecretKeySpec(key.getBytes(), BLOWFISH);
			}
			cipherBlowfish.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] ret=cipherBlowfish.doFinal(digestRet);
			//最后利用BASE64转为字符串显示。
			return new String(Base64.encode(ret));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 对某个文本进行签名
	 * 
	 * @param source String 文本
	 * @param key String 秘钥
	 * @return String 前面结果
	 * @author 榕基党员E家项目组
	 */
	public static String sign(String source, String key) {
		if (source == null || source.equals("")) {
			return "";
		}
		try {
			return sign(new ByteArrayInputStream(source.getBytes(ENCODING)), key);
		} catch (UnsupportedEncodingException e) {
			return sign(new ByteArrayInputStream(source.getBytes()), key);
		}
	}

}

