package com.dasset.wallet.core.wallet;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;

import javax.annotation.Nullable;

/**
 * ECC加密过程：
 * K(公钥) = k(私钥) * G(基点);
 * 把明文编码成曲线上的点M；
 * 生成一个随机数r；
 * 计算密文C1=M + r*K(公钥), C2 = r*G；
 * 对方收到密文后，可以计算C1 - k(私钥)C2 = M；
 * 攻击者得到C1、 C2，公钥K以及基点G，没有私钥无法计算出M。
 * <p>
 * 1.生成随机私钥
 * 2.椭圆曲线算公钥
 * 3.计算公钥的SHA-256哈希值
 * 4.计算 RIPEMD-160哈希值
 * 5.加入地址版本号（比特币主网版本号“0x00”）
 * 6.计算SHA-256哈希值
 * 7.取6.结果的前4个字节（8位十六进制）
 * 8.把这4个字节加在第五步的结果后面
 * 9.用Base58编码变换一下地址
 * https://www.zhihu.com/question/22399196
 */
public final class ECKeyPairFactory {

    private BigInteger privateKey;
    private byte[] publicKey;

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    private ECKeyPairFactory(@Nullable BigInteger privateKey, @Nullable byte[] publicKey, boolean compressed) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        if (privateKey == null && publicKey == null) {
            throw new IllegalArgumentException("ECKeyPairFactory requires at least private or public key");
        }
        this.privateKey = privateKey;
        if (publicKey != null) {
            this.publicKey = publicKey;
        } else {
            this.publicKey = generatePublicKey(privateKey, compressed);
        }
    }

    private ECKeyPairFactory(@Nullable BigInteger privateKey, @Nullable byte[] publicKey) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        this(privateKey, publicKey, false);
    }

//    public ECKeyPairFactory(BigInteger privateKey, BigInteger publicKey, boolean compressed) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
//        this(privateKey, Utils.bigIntegerToBytes(publicKey, 65), compressed);
//    }

    public ECKeyPairFactory(@Nullable byte[] privateKey, @Nullable byte[] publicKey, boolean compressed) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        this(privateKey == null ? null : new BigInteger(1, privateKey), publicKey, compressed);
    }

    public static byte[] generatePublicKey(BigInteger privateKey, boolean compressed) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if (privateKey != null) {
            X9ECParameters x9ECParameters = SECNamedCurves.getByName("secp256k1");
            ECDomainParameters ecDomainParameters = new ECDomainParameters(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH());
            ECPoint ecPoint = ecDomainParameters.getG().multiply(privateKey);
            return new ECPoint.Fp(ecDomainParameters.getCurve(), ecPoint.getX(), ecPoint.getY(), compressed).getEncoded();
        }
        return null;
    }

    public static ECKeyPairFactory generateECKeyPair(boolean compressed) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        //Add bouncy castle as key pair gen provider
//        Security.addProvider(new BouncyCastleProvider());
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        //Generate key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "SC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        //Convert KeyPair to ECKeyPairFactory, to store keys as BigIntegers
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new ECKeyPairFactory(((BCECPrivateKey) keyPair.getPrivate()).getD(), ((BCECPublicKey) keyPair.getPublic()).getQ().getEncoded(compressed), compressed);
    }

    public static ECKeyPairFactory generateECKeyPair(BigInteger privateKey, boolean compressed) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return new ECKeyPairFactory(privateKey, null, compressed);
    }
}
