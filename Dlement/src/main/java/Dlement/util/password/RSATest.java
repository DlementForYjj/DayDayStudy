//package Dlement.util.password;
//
//import java.io.ByteArrayOutputStream;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidKeyException;
//import java.security.KeyFactory;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.Signature;
//import java.security.SignatureException;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Map;
//import java.util.TreeMap;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//
//import org.apache.commons.codec.binary.Base64;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//public class RSATest {
//	private static String RSA = "RSA";  
//	private static String RSASHA = "SHA1WithRSA";
////	encode_public.key为公钥，sign_private.key为私钥
//	 /**
//     * RSA最大加密明文大小  
//     */    
//    private static final int MAX_ENCRYPT_BLOCK = 117;
//    private static final int KEY_LENGTH = 1024;
//    
//	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
//
//		KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
//		kpg.initialize(KEY_LENGTH);
//		KeyPair pair = kpg.genKeyPair();
//		
////		pair = new KeyPair(getPublicKey("encode_public.key"), getPrivateKey("sign_private.key"));
//		
//		String str = "你好呀";
//		String signed = sign(pair.getPrivate());
//		System.out.println(checkSign(pair.getPublic(), signed));
//		str = "{\"head\":{\"token\":\"1V1PTRTWPAXDL8Q6X74AMG929P3CA2FB\"},\"content\":{\"idCard\":\"350000000000000022\",\"sign\":\"epg0HPIFTDb/1IqG8kDiEhRGcZycxUrdgoUtYDGLaZodWr5eeaWTl5Zde7heXA7YtRo6Vg8J4SXEJBQ0Fh3aJx9i7CnWcJXRuq28zF92wRDwZu5AkRdBJS/KmvpzG40xaFhwOTzxCU6gOSXHJxGIGqanLAXIrz1CwmSTAsSZdeQ\u003d\"}}";
//		
//
//		byte[] encryptedData =  encryptData(str.getBytes(), pair.getPublic());
//		System.out.println(new String(encryptedData));
//		byte[] decryptedData = decryptData(encryptedData, pair.getPrivate());
////		System.out.println(new String(decryptedData));
//		
//		
//	}
//	
//	/**
//	 * 公钥加密
//	 * @param data
//	 * @param publicKey
//	 * @return
//	 */
//	public static byte[] encryptData(byte[] data, PublicKey publicKey) {
//
//		try {
//			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//			// 编码前设定编码方式及密钥
//			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//			// 传入编码数据并返回编码结果
//			int inputLen = data.length;
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			int offSet = 0;
//			byte[] cache;
//			int i = 0;
//			// 对数据分段加密
//			while (inputLen - offSet > 0) {
//				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
//					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
//				} else {
//					cache = cipher.doFinal(data, offSet, inputLen - offSet);
//				}
//				out.write(cache, 0, cache.length);
//				i++;
//				offSet = i * MAX_ENCRYPT_BLOCK;
//			}
//			byte[] encryptedData = out.toByteArray();
//			out.close();
//			return encryptedData;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * 用私钥解密
//	 * 
//	 * @param encryptedData
//	 *            经过encryptedData()加密返回的byte数据
//	 * @param privateKey
//	 *            私钥
//	 * @return
//	 */
//	public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
//		try {
//			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//			cipher.init(Cipher.DECRYPT_MODE, privateKey);
//			return cipher.doFinal(encryptedData);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	/**
//	 * 反向获取
//	 * @param publicKeyStr
//	 * @return
//	 * @throws InvalidKeySpecException
//	 * @throws NoSuchAlgorithmException
//	 * @throws NoSuchPaddingException
//	 * @throws InvalidKeyException
//	 * @throws IllegalBlockSizeException
//	 * @throws BadPaddingException
//	 * @throws UnsupportedEncodingException
//	 */
//	public static PublicKey getPublicKey(String publicKeyStr) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
//		/*公钥字符串*/
//		/*X.509 标准中定义的公钥编码标准，使用这个类进行转换*/
//		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));
//		//获得公钥对象
//		PublicKey publicKey = KeyFactory.getInstance(RSA).generatePublic(x509KeySpec);
//		return publicKey;
//	}
//	 /**
//     * 得到私钥
//     * @param privateKey 密钥字符串（经过base64编码）
//     * @throws Exception
//     */
//    public static PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        //通过PKCS#8编码的Key指令获得私钥对象
//        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
//        PrivateKey key = keyFactory.generatePrivate(pkcs8KeySpec);
//        return key;
//    }
//	public static final Gson GSON = new Gson();
//	
//	public static String sign(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException{
//		TreeMap<String, String> map = new TreeMap<String, String>();
//		map.put("msg1", "1324679");
//		map.put("msg2", "A001");
//		map.put("msg3", "V1.0");
//		map.put("msg4", "");
//		/* 2. 计算验签字段, sign字段不参与签名 map转String方法可以自定义*/
//		 String line =GSON.toJson(map);
//		//设置签名加密方式
//		Signature signature = Signature.getInstance(RSASHA);
//		signature.initSign(privateKey);//设置私钥
//		//签名和加密一样 要以字节形式 utf-8字符集得到字节
//		signature.update(line.getBytes("UTF-8"));
//		//得到base64编码的签名后的字段
//		String sign = Base64.encodeBase64String(signature.sign());
//		//将签名放入map
//		map.put("sign", sign);
//		//将要传输的数据转成json
//		return GSON.toJson(map);
//	}
//	
//	public static boolean checkSign(PublicKey publicKey,String requestInfo) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, UnsupportedEncodingException{
//		/*获得请求过来的request（若加密请解密）并转换成map*/
//		Map<String, String> map = GSON.fromJson(requestInfo, new TypeToken<Map<String,String>>(){}.getType());
//		String sign = map.get("sign");//获得签名
//		map.remove("sign");//剔除签名
//		String line = GSON.toJson(map);//转化为字符串
//		//指定签名类型
//		Signature signature = Signature.getInstance(RSASHA);
//		signature.initVerify(publicKey);//放入公钥
//		signature.update(line .getBytes("UTF-8"));//放入数据
//		//验签结果
//		boolean isPassed= signature.verify(Base64.decodeBase64(sign));
//		return isPassed;
//	}
//}
