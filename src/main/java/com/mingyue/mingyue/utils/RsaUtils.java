package com.mingyue.mingyue.utils;




import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import java.util.HashMap;
import java.util.Map;

public class RsaUtils{
    //RSA 获取公钥的key 和 前端的私钥配对
    public static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUxes6RJbgtWm1heXsvbPr4fdBbCdvPEQ5bkuprZTtAaw3Us0u0YwDbBHQVEuli4uhRj5LysQR+DezndI6mjAwcOmmpz1qxb5k+G5ZRfSqOYL3Pib8CDlI0RPGas0Ernm7yqHKW28Lq/Z+DhX/54pWOoqgaCIsDt/tqY6ViHrxhQIDAQAB";
    //RSA 获取私钥的key 和 前端的公钥配对
    public static final String RSA_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKONwwsx4NYKcYFUqjNQlxZ+SYhJNC2xu00Aono/gQ8BvlEJX8Nrlsnq00M0FwR2uc9lSj1MeyZAvtxT0OXcBvB/jbW4UqlSPJ5K3eeeQAnEyrjO8RmvseoMypfew/rHF5vcHGupHdoER/Ze7nRmK1D6etjS7W5x1XvSziqKwaahAgMBAAECgYBEf8MIlUNNYeJYAFWoWEBvM0Uox0ALQzzm6zUhbidnWZuT5REuj4j/2FHS+6YIQJFhw1JsifOp+cP2E0SSgKyGryt4JsRQ1BWGUzGBmheKrNj4VBfpt1TzH8kTOAPqiZDcYwAkqww/AEwku2vPmm324XV8qIlYcoujxjT3eESkYQJBAPVO5o/U3wOiklb354XL9xPlNEZLHNuFvKt/cCbEJea/VN+W4G7xeKUDggFHLSQfvaXf6pJP85PLgxHyMEcTNDsCQQCqrqlh+hnEW5KMom4eCe1+DPCtNPfWMxfr9iRZ2EMt1j6X+X8vRW52OBirZV27sDxpKGqAuh9awVBCFWCV8S7TAkEAtQ0g2nNwjgxFGe8qb/PUTljStjbl+5e/YoxSSCppFt+MUuHWj8ulEZV/NFZGGO1cWbrkNEzSJ/kCqhIoU6z21QJAFa3BjdQ6WZpX7szit6YDKDN0jktf/zDWJP5Kd67kOXow0RS7dsGW2iUd3Qcu9JvZ0HF1tYvuV5SeIQaeEe1kSQJAat7f4rRcO0jdet4OZQdyo2c4NrdmtI55s8SM30IpeKtknIVcrXL71ueMDXYPOHEHZToKqqgNjtmum6v6qnIwHg==";
    // 加密算法RSA
    public static final String KEY_ALGORITHM = "RSA";
    //初始化密钥大小
    public static final Integer INITIALIZE = 1024;
    //RSA最大加密明文大小
    public static final Integer MAX_ENCRYPT_BLOCK = 117;
    //RSA最大解密密文大小
    public static final Integer MAX_DECRYPT_BLOCK = 128;
    //签名算法
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 生成密钥对(公钥和私钥)
     */
    public static Map<String, Object> generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(INITIALIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new HashMap<String, Object>(){{
            put(RSA_PUBLIC_KEY, publicKey);
            put(RSA_PRIVATE_KEY, privateKey);
        }};
    }

    /**
     * 获取公钥
     * @param keyMap 密钥对
     */
    public static String getRSAPublicKey(Map<String, Object> keyMap){
        Key key = (Key) keyMap.get(RSA_PUBLIC_KEY);
        return Base64Util.encode(key.getEncoded());
    }

    /**
     * 获取私钥
     * @param keyMap 密钥对
     */
    public static String getRSAPrivateKey(Map<String, Object> keyMap){
        Key key = (Key) keyMap.get(RSA_PRIVATE_KEY);
        return Base64Util.encode(key.getEncoded());
    }

    /**
     * 公钥加密
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64Util.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 私钥解密
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64Util.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }


    /**
     * 公钥解密
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64Util.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 私钥加密
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Util.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 用私钥对信息生成数字签名
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Util.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Util.encode(signature.sign());
    }

    /**
     * 校验数字签名
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64Util.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Util.decode(sign));
    }


    public static void main(String[] args) throws Exception{

        String json = "你好";

        //公钥加密
        System.out.println("********************公钥加密、生成数字签名、数字签名校验、私钥解密********************");
        System.out.println("字符串：" + json);
        String encryptPublic = Base64Util.encode(encryptByPublicKey(json.getBytes(StandardCharsets.UTF_8), RsaUtils.RSA_PUBLIC_KEY));
        System.out.println("公钥加密后字符串：" + encryptPublic);

        String s = new String(
                decryptByPrivateKey(Base64Util.decode(encryptPublic),"MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJTF6zpEluC1abWF5ey9s+vh90FsJ288RDluS6mtlO0BrDdSzS7RjANsEdBUS6WLi6FGPkvKxBH4N7Od0jqaMDBw6aanPWrFvmT4bllF9Ko5gvc+JvwIOUjRE8ZqzQSuebvKocpbbwur9n4OFf/nilY6iqBoIiwO3+2pjpWIevGFAgMBAAECgYAAmBzFJg57NcZHDxKYxDYygDvKU1oB9/nmx9G+Cv8wBiRvsSE4KWaXgeg9ToTh06GaYosvps7rKnRgf4YeLUz5dv6mWmoJ8JAb1UrWXyPCJBxDXoaQVhAFWmb/VT504r/tGUBDgL3q9hCoetEQ0Q+zV8mG0W8l1lpyWhVw8cOiMQJBAMpCnkMt7YSEpR3tfIYoSATwtAiSrejZjHAVPdQEaoeHthN4qTwlwtJ4+sNeLjDBnYMx3RrXGxy3FwzQg4EFQ7UCQQC8TTPZxkebpW9DOwRhx7U40lf0XHKs1uTMPCn5CYOzO4Xor5rhv22K4vCclXUqa2nAhWemU9jz1nugH5oE8DiRAkEAx3rElAUsCFK6drXO5pG71gN/zCS+GLTW6YRJol1oo5qZXMLYRlYHu4VN7HlqnPOX4di/9MTKHJwGw8CysgD7MQJBALMotWPH3wv9XJ5YAbRTAl7860iJyWYf1HNp7vmPDuqygJdCtZeCvmP9xrcFEQR6TJUrmNHNR0fBU8CnMLDwKrECQQDGXSTXm7E1WrUZ7GkwuwxWeiPa5MVAQ2Iw/PbufU1pYgY/0fO3p3IznlNWkX7hi16fWFD2lSs4z7GxjvRaq58k"));

        System.err.println(s);

        String pa = "UktpYeKOu9c/gNO4sa5wF2YHG50nDsyl0VVWdCgo4d+jqRulrepcd6xwnYsD+EI/wWAHB1fsvdu2q/8C527OXZ54JeuntstHUlnkeS/wZyMXIVLOl4fNDKQZhhaW9SerCAZnwn2/2WgeKNAdhw54AZ9zyc/SFloK6WWKzGIBBs8=";

        System.err.println(new String(decryptByPrivateKey(Base64Util.decode(pa),RSA_PRIVATE_KEY)));


//        //生成数字签名
//        String signKey = sign(Base64Util.decode(encryptPublic), privateKey);
//        System.out.println("公钥加密后数字签名：" + signKey);
//        //校验数字签名
//        boolean verify = verify(Base64Util.decode(encryptPublic), publicKey, signKey);
//        System.out.println("公钥加密后数字签名校验：" + verify);


    }


}
