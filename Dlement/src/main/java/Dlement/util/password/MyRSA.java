//package Dlement.util.password;
//import java.io.ByteArrayOutputStream;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidKeyException;
//import java.security.Key;
//import java.security.KeyFactory;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.Signature;
//import java.security.SignatureException;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.TreeMap;
//
//import javax.crypto.Cipher;
//
//import com.google.common.reflect.TypeToken;
//import com.google.gson.Gson;
//
//public class MyRSA {
//
//    public static final String CHARSET = "UTF-8";
//    public static final String RSA_ALGORITHM = "RSA";
//
//
//    public static void main (String[] args) throws Exception {
//        Map<String, String> keyMap = MyRSA.createKeys(1024);
//        String  publicKey = keyMap.get("publicKey");
//        String  privateKey = keyMap.get("privateKey");
////        System.out.println(publicKey.length()+privateKey.length());
//        System.out.println("公钥: \n\r" + publicKey);
//        System.out.println("私钥： \n\r" + privateKey);
//
//        System.out.println("公钥加密——私钥解密");
//        String str = "{\"head\":{\"token\":\"1V1PTRTWPAXDL8Q6X74AMG929P3CA2FB\"},\"content\":{\"idCard\":\"350000000000000022\",\"sign\":\"epg0HPIFTDb/1IqG8kDiEhRGcZycxUrdgoUtYDGLaZodWr5eeaWTl5Zde7heXA7YtRo6Vg8J4SXEJBQ0Fh3aJx9i7CnWcJXRuq28zF92wRDwZu5AkRdBJS/KmvpzG40xaFhwOTzxCU6gOSXHJxGIGqanLAXIrz1CwmSTAsSZdeQ\u003d\"}}";
//        System.out.println("\r明文：\r\n" + str);
//        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
//        String encodedData = MyRSA.publicEncrypt(str, MyRSA.getRSAPublicKey(publicKey));
//        System.out.println("密文：\r\n" + encodedData);
//        String decodedData = MyRSA.privateDecrypt(encodedData, MyRSA.getRSAPrivateKey(privateKey));
//        System.out.println("解密后文字: \r\n" + decodedData);
//        
//    	String signed = sign(MyRSA.getPrivateKey(privateKey));
//    	System.out.println("签名sign的内容: \r\n"+signed);
//		System.out.println(checkSign(MyRSA.getPublicKey(publicKey), signed));
//    }
//    
//    public static Map<String, String> createKeys(int keySize){
//        //为RSA算法创建一个KeyPairGenerator对象
//        KeyPairGenerator kpg;
//        try{
//            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
//        }catch(NoSuchAlgorithmException e){
//            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
//        }
//
//        //初始化KeyPairGenerator对象,密钥长度
//        kpg.initialize(keySize);
//        //生成密匙对
//        KeyPair keyPair = kpg.generateKeyPair();
//        //得到公钥
//        Key publicKey = keyPair.getPublic();
//        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
//        //得到私钥
//        Key privateKey = keyPair.getPrivate();
//        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
//        Map<String, String> keyPairMap = new HashMap<String, String>();
//        keyPairMap.put("publicKey", publicKeyStr);
//        keyPairMap.put("privateKey", privateKeyStr);
//
//        return keyPairMap;
//    }
//
//    /**
//     * 得到公钥
//     * @param publicKey 密钥字符串（经过base64编码）
//     * @throws Exception
//     */
//    public static RSAPublicKey getRSAPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        //通过X509编码的Key指令获得公钥对象
//        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
//        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
//        return key;
//    }
//    /**
//     * 得到私钥
//     * @param privateKey 密钥字符串（经过base64编码）
//     * @throws Exception
//     */
//    public static RSAPrivateKey getRSAPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        //通过PKCS#8编码的Key指令获得私钥对象
//        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
//        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
//        return key;
//    }
//    /**
//     * 得到私钥
//     * @param privateKey 密钥字符串（经过base64编码）
//     * @throws Exception
//     */
//    public static PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        //通过PKCS#8编码的Key指令获得私钥对象
//        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
//        return keyFactory.generatePrivate(pkcs8KeySpec);
//    }
//    /**
//     * 得到公钥
//     * @param publicKey 密钥字符串（经过base64编码）
//     * @throws Exception
//     */
//    public static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        //通过X509编码的Key指令获得公钥对象
//        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
//        return keyFactory.generatePublic(x509KeySpec);
//    }
//
// 
//    /**
//     * 公钥加密
//     * @param data
//     * @param publicKey
//     * @return
//     */
//    public static String publicEncrypt(String data, RSAPublicKey publicKey){
//        try{
//            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
//        }catch(Exception e){
//            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
//        }
//    }
//
//    /**
//     * 私钥解密
//     * @param data
//     * @param privateKey
//     * @return
//     */
//
//    public static String privateDecrypt(String data, RSAPrivateKey privateKey){
//        try{
//            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, privateKey);
//            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
//        }catch(Exception e){
//            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
//        }
//    }
//
//    /**
//     * 私钥加密
//     * @param data
//     * @param privateKey
//     * @return
//     */
//
//    public static String privateEncrypt(String data, RSAPrivateKey privateKey){
//        try{
//            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
//            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
//        }catch(Exception e){
//            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
//        }
//    }
//
//    /**
//     * 公钥解密
//     * @param data
//     * @param publicKey
//     * @return
//     */
//
//    public static String publicDecrypt(String data, RSAPublicKey publicKey){
//        try{
//            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, publicKey);
//            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
//        }catch(Exception e){
//            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
//        }
//    }
//
//    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
//        int maxBlock = 0;
//        if(opmode == Cipher.DECRYPT_MODE){
//            maxBlock = keySize / 8;
//        }else{
//            maxBlock = keySize / 8 - 11;
//        }
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] buff;
//        int i = 0;
//        try{
//            while(datas.length > offSet){
//                if(datas.length-offSet > maxBlock){
//                    buff = cipher.doFinal(datas, offSet, maxBlock);
//                }else{
//                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
//                }
//                out.write(buff, 0, buff.length);
//                i++;
//                offSet = i * maxBlock;
//            }
//        }catch(Exception e){
//            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
//        }
//        byte[] resultDatas = out.toByteArray();
//        return resultDatas;
//    }
//
//    
//    public static final Gson GSON = new Gson();
//    private static String RSASHA = "SHA1WithRSA";
//    public static String sign(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException{
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