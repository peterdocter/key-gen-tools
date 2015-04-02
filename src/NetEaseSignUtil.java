/*
 * @(#)NetEaseSignUtil.java, 2015-2-3.
 *
 * Copyright 2015 Yodao, Inc. All rights reserved.
 * YODAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
// package outfox.buyers.service.api.neteasepay;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class NetEaseSignUtil
{
	public static String priKey_Hex_Str = "30820277020100300d06092a864886f70d0101010500048202613082025d020100028181009d79668956de3170afe44136964f9b76220fbe01a320d84f24eefa8fd87f42d3d74b14ee39eb2adcdab107db940b4d915e07b9da9475a58ca523f42463bcf3f0207b232d444bc3549c7fee94850ffabfb99fce446536782430b47fe277fb8bc9fb3a5c36f3b8b900bfa5ad539818115c4aebbb0af2ef68c4ef344220c0c993c702030100010281805235dcc7fa67c95bd117d2cbd2b100b22d9a1651f87ddf2f825298d09befe44d7fea1ca539eb81d706fcc0e36b6873385ee49dac7530ee1a7ce44642f126e61a36ca9c03542d7bbc5797de4439b291cd07efa6a56bc9ef7d8fe0fd4710b17aef93a75131bf40b6ee43077a4caace79f2f1af9d05fb2ad45506a569c7ee574041024100cd0a42118ce8a606decb6b29d78e9add624584c496f458932115b935f688d9ac8bc18a4e0b2bca8963379bda3b051e77de8786a29d17f31ff67c459aed322757024100c49cbfe6f6ef96903be2a67e3c9f05f31e58e7efd00f11c20f58c34a9b73d5fa3076d2c51093d42e5951baf28dca51dcb82737f13724a2cb36be6b02f5bb611102410084959f874dc09aab52a1e317d965434ae862dfa9679b75331e6fb7a5e75732ba5a5cba1c21291dfc9d05b667f546281f86ebc7e99aff839d257bd25e8e06a25d024009c252102fcfa2a4942462e93f2f118788c916f2235cac1ffacd081c8aeccc0138289dbba46002172eddccaebf74fc6d3d42cd170f0aa57dfac0a6e525b76051024100b7a0d4c1ebf449cb4bfa87893f458cc9319504204199ba78bf0829b0037aa0094303bcff73259cae1b73bb99e4e2cfd5d961b0574965e9565bdcb178371f9b82";
	private static String pubKey_Hex_Str = "30819f300d06092a864886f70d010101050003818d00308189028181009d79668956de3170afe44136964f9b76220fbe01a320d84f24eefa8fd87f42d3d74b14ee39eb2adcdab107db940b4d915e07b9da9475a58ca523f42463bcf3f0207b232d444bc3549c7fee94850ffabfb99fce446536782430b47fe277fb8bc9fb3a5c36f3b8b900bfa5ad539818115c4aebbb0af2ef68c4ef344220c0c993c70203010001";

	public static void main(String[] args)
	{
		try
		{
			//产生公私钥
			NetEaseSignUtil.genRSAKeyPair();

			//签名验签
			String srcToVeryfy = URLEncoder.encode("urstestc@163.com1赵国强赵国强20101227105117201012271131591203.86.63.98203.86.63.9854416","UTF-8");
			//System.out.println("srcToVeryfy=="+srcToVeryfy);
			//String sign = "6a8def299fd13dcaa82a7a1abc6c969ed2bdce283b47965afd5cb20caa08fbe8c71d076f73960f35164483021246e4878323e990911ea6ead62393d148cbbffbe4317de64fa4c8029ed3bf8a46babd7d05e4271d5a710cdd21a3a3db6cd8e42d62498a5ea50c0fe9b5bd1dfcf72ae44822a3454f8f92e190980d18108be10bdc";
			//	System.out.println("签名为：" + sign);
			
			String sign = NetEaseSignUtil.generateSHA1withRSASigature(priKey_Hex_Str, srcToVeryfy);
			System.out.println("原始串：" + srcToVeryfy);
			System.out.println("签名：" + sign);
			boolean result = NetEaseSignUtil.verifySHA1withRSASigature(pubKey_Hex_Str, sign, srcToVeryfy);
			System.out.println("验证签名结果为：" + result);
		
			//加密
			String srcToEncode = "123_~8*Hell赵刚媉";
			String encoded = NetEaseSignUtil.encode(srcToEncode, pubKey_Hex_Str);
			//解密
			NetEaseSignUtil.decode(encoded, priKey_Hex_Str);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * 本方法使用SHA1withRSA签名算法产生签名
	 * @param String priKey 签名时使用的私钥(16进制编码)
	 * @param String src	签名的原字符串
	 * @return String 		签名的返回结果(16进制编码)。当产生签名出错的时候，返回null。
	 */
	public static String generateSHA1withRSASigature(String priKey, String src)
	{
		try
		{

			Signature sigEng = Signature.getInstance("SHA1withRSA");

			byte[] pribyte = hexStrToBytes(priKey.trim());

			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");

			RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
			sigEng.initSign(privateKey);
			sigEng.update(src.getBytes());

			byte[] signature = sigEng.sign();
			return bytesToHexStr(signature);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//LogMan.log("[NeteaseSignUtil][generateSHA1withRSASigature]"+e);
			return null;
		}
	}

	/**
	 * 本方法使用SHA1withRSA签名算法验证签名
	 * @param String pubKey 验证签名时使用的公钥(16进制编码)
	 * @param String sign 	签名结果(16进制编码)
	 * @param String src	签名的原字符串
	 * @return String 		签名的返回结果(16进制编码)
	 */
	public static boolean verifySHA1withRSASigature(String pubKey, String sign, String src)
	{
		try
		{
			Signature sigEng = Signature.getInstance("SHA1withRSA");

			byte[] pubbyte = hexStrToBytes(pubKey.trim());

			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);

			sigEng.initVerify(rsaPubKey);
			sigEng.update(src.getBytes());

			byte[] sign1 = hexStrToBytes(sign);
			return sigEng.verify(sign1);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			//LogMan.log("[NeteaseSignUtil][verifySHA1withRSASigature]"+e);
			return false;
		}
	}

	/**
	 * 本方法用于产生1024位RSA公私钥对。
	 * 
	 */
	public static void genRSAKeyPair()
	{
		KeyPairGenerator rsaKeyGen = null;
		KeyPair rsaKeyPair = null;
		try
		{
			System.out.println("Generating a pair of RSA key ... ");
			rsaKeyGen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = new SecureRandom();
			random.setSeed(("" + System.currentTimeMillis() * Math.random() * Math.random()).getBytes());
			rsaKeyGen.initialize(1024, random);
			rsaKeyPair = rsaKeyGen.genKeyPair();
			PublicKey rsaPublic = rsaKeyPair.getPublic();
			PrivateKey rsaPrivate = rsaKeyPair.getPrivate();
			System.out.println("公钥：" + bytesToHexStr(rsaPublic.getEncoded()));
			System.out.println("私钥：" + bytesToHexStr(rsaPrivate.getEncoded()));
			System.out.println("1024-bit RSA key GENERATED.");
		}
		catch (Exception e)
		{
			System.out.println("genRSAKeyPair：" + e);
		}
	}

	/**
	 * 公钥加密
	 * @param srcToEncode
	 * @param pubKey
	 * @return
	 */
	public static String encode(String srcToEncode, String pubKey)
	{

		try
		{
			System.out.println("需要加密的明文：" + srcToEncode);
			byte[] pubbyte = hexStrToBytes(pubKey.trim());
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);

			BigInteger e = rsaPubKey.getPublicExponent();
			BigInteger n = rsaPubKey.getModulus();
			System.out.println("加密的E：" + e);
			System.out.println("加密的n：" + n);
			//获得明文 ming
			byte[] plainText = srcToEncode.getBytes("UTF-8");
			BigInteger ming = new BigInteger(plainText);
			//计算密文 coded
			BigInteger coded = ming.modPow(e, n);
			System.out.println("密文：" + coded);
			return coded.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 私钥解密
	 * @param encodedSrc
	 * @param prikey
	 * @return
	 */
	public static String decode(String encodedSrc,String priKey)
	{
		try
		{
			byte[] pribyte = hexStrToBytes(priKey.trim());
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
			
			//获得私钥参数
			BigInteger n = privateKey.getModulus();
			BigInteger d = privateKey.getPrivateExponent();
			System.out.println("解密n为："+n);
			System.out.println("解密d为："+d);
			//密文
			BigInteger coded = new BigInteger(encodedSrc);
			BigInteger m =coded.modPow(d, n);
			//打印解密结果
			byte[] result = m.toByteArray();
			String str = new String(result,"UTF-8");
			System.out.println("明文解密结果为："+str);
			
			return str;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//LogMan.log("[NeteaseSignUtil][generateSHA1withRSASigature]"+e);
			return null;
		}
	}
	
	/**
	 * 将字节数组转换为16进制字符串的形式.
	 */
	private static final String bytesToHexStr(byte[] bcd)
	{
		StringBuffer s = new StringBuffer(bcd.length * 2);

		for (int i = 0; i < bcd.length; i++)
		{
			s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
			s.append(bcdLookup[bcd[i] & 0x0f]);
		}

		return s.toString();
	}

	/**
	 * 将16进制字符串还原为字节数组.
	 */
	private static final byte[] hexStrToBytes(String s)
	{
		byte[] bytes;

		bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
		}

		return bytes;
	}

	private static final char[] bcdLookup =
	{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

}
