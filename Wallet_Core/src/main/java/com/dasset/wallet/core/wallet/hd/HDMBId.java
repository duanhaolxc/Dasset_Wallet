package com.dasset.wallet.core.wallet.hd;

import com.dasset.wallet.core.AddressManager;
import com.dasset.wallet.core.api.GetHDMBIdRandomApi;
import com.dasset.wallet.core.api.RecoveryHDMApi;
import com.dasset.wallet.core.api.UploadHDMBidApi;
import com.dasset.wallet.core.api.http.HttpException;
import com.dasset.wallet.core.crypto.EncryptedData;
import com.dasset.wallet.core.crypto.ECKey;
import com.dasset.wallet.core.db.facade.BaseProvider;
import com.dasset.wallet.core.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.List;
import java.util.Locale;

public class HDMBId {
    private static final Logger log = LoggerFactory.getLogger(HDMBId.class);

    public final static String BITID_STRING = "bitid://hdm.bither.net/%s/password/%s/%d";
    private static HDMBId hdmbidCache;

    private String        address;
    private EncryptedData encryptedBitherPassword;
    private byte[]        decryptedPassword;
    private long          serviceRandom;


    public HDMBId(String address) {
        this.address = address;
    }


    public HDMBId(String addressForBitherId, String encryptedBitherPassword) {
        this(addressForBitherId, new EncryptedData(encryptedBitherPassword));
    }


    public HDMBId(String addressForBitherId, EncryptedData encryptedBitherPassword) {
        this.address = addressForBitherId;
        this.encryptedBitherPassword = encryptedBitherPassword;
    }

    public String getPreSignString() throws Exception {
        SecureRandom random = new SecureRandom();
        decryptedPassword = new byte[32];
        random.nextBytes(decryptedPassword);
        GetHDMBIdRandomApi getHDMBIdRandomApi = new GetHDMBIdRandomApi(address);
        getHDMBIdRandomApi.handleHttpGet();
        serviceRandom = getHDMBIdRandomApi.getResult();
        String message = getBitidString();
        byte[] hash = Utils.getPreSignMessage(message);
        return Utils.bytesToHexString(hash);


    }

    public String setSignatureAndGetAddressOfAddressOfSp(byte[] signed, CharSequence password, String firstHotAddress) throws Exception {
        String addressOfSP = null;
        String message     = getBitidString();
        byte[] hash        = Utils.getPreSignMessage(message);
        ECKey  key         = ECKey.signedMessageToKey(hash, signed);
        if (Utils.compareString(address, key.toAddress())) {
            throw new SignatureException();

        }
        String          hotAddress      = firstHotAddress != null ? firstHotAddress : AddressManager.getInstance().getHdmKeychain().getFirstAddressFromDb();
        UploadHDMBidApi uploadHDMBidApi = new UploadHDMBidApi(address, hotAddress, signed, decryptedPassword);
        uploadHDMBidApi.handleHttpPost();
        boolean result = uploadHDMBidApi.getResult();
        if (result) {
            encryptedBitherPassword = new EncryptedData(decryptedPassword, password);
            ECKey k = new ECKey(decryptedPassword, null);
            addressOfSP = k.toAddress();
            k.clearPrivateKey();
            if (firstHotAddress == null) {
                save(addressOfSP);
            }
        } else {
            throw new HttpException("UploadHDMBidApi error");
        }
        return addressOfSP;
    }

    public void setSignature(String signString, CharSequence password) throws Exception {
        setSignatureAndGetAddressOfAddressOfSp(Utils.hexStringToByteArray(signString), password, null);
    }

    public void save(String addressOfPS) {
        BaseProvider.iAddressProvider.addAndUpdateHDMBId(HDMBId.this, addressOfPS);
    }

    public List<HDMAddress.Pubs> recoverHDM(String signString, CharSequence secureCharSequence) throws Exception {
        String message = getBitidString();
        byte[] hash = Utils.getPreSignMessage(message);
        ECKey key = ECKey.signedMessageToKey(hash, Utils.hexStringToByteArray(signString));
        if (Utils.compareString(address, key.toAddress())) {
            throw new SignatureException();

        }

        RecoveryHDMApi recoveryHDMApi = new RecoveryHDMApi(address, Utils.hexStringToByteArray(signString), decryptedPassword);
        recoveryHDMApi.handleHttpPost();
        List<HDMAddress.Pubs> result = recoveryHDMApi.getResult();
        ECKey k = new ECKey(decryptedPassword, null);
        String address = k.toAddress();
        k.clearPrivateKey();
        encryptedBitherPassword = new EncryptedData(decryptedPassword, secureCharSequence);
        BaseProvider.iAddressProvider.addAndUpdateHDMBId(HDMBId.this, address);
        return result;


    }

    private String getBitidString() {
        return Utils.format(BITID_STRING, address, Utils.bytesToHexString(decryptedPassword).toLowerCase(Locale.US), serviceRandom);

    }

    public String getAddress() {
        return address;
    }


    public String getEncryptedBitherPasswordString() {
        return encryptedBitherPassword.toEncryptedString();
    }

    public void setEncryptedData(EncryptedData encryptedData) {
        this.encryptedBitherPassword = encryptedData;
    }


    public byte[] decryptHDMBIdPassword(CharSequence password) {
        HDMBId hdmbId = BaseProvider.iAddressProvider.getHDMBId();
        if (!Utils.isEmpty(hdmbId.getEncryptedBitherPasswordString())) {
            encryptedBitherPassword = new EncryptedData(hdmbId.getEncryptedBitherPasswordString());
        }
        decryptedPassword = encryptedBitherPassword.decrypt(password);
        return decryptedPassword;
    }


    public synchronized static HDMBId getHDMBidFromDb() {
        if (hdmbidCache != null) {
            return hdmbidCache;
        }
        hdmbidCache = BaseProvider.iAddressProvider.getHDMBId();
        if (hdmbidCache == null || Utils.isEmpty(hdmbidCache.getAddress()) ||
                Utils.isEmpty(hdmbidCache.getEncryptedBitherPasswordString())) {
            hdmbidCache = null;
        }
        return hdmbidCache;
    }

}
