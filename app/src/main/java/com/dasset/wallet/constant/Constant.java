package com.dasset.wallet.constant;

import android.Manifest;

public final class Constant {

    private Constant() { }

    public static final String   FILE_PROVIDER_AUTHORITY = "com.dasset.wallet.fileprovider";
    public static final String[] PERMISSIONS             = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.CAMERA
            , Manifest.permission.READ_SMS
            , Manifest.permission.READ_LOGS
            , Manifest.permission.CALL_PHONE
    };

    public final static class RequestCode {
        public static final int DIALOG_PROMPT_SET_PERMISSION           = 0x1000;
        public static final int PREMISSION_SETTING                     = 0x1001;
        public static final int DIALOG_PROMPT_SET_NET_WORK             = 0x1002;
        public static final int NET_WORK_SETTING                       = 0x1003;
        public static final int DIALOG_PROMPT_QUIT                     = 0x1004;
        public static final int DIALOG_PROGRESS_WALLET                 = 0x1005;
        public static final int INSTALL_APK                            = 0x1006;
        public static final int DIALOG_PROMPT_REQUEST_DATA_ERROR       = 0x1007;
        public static final int DIALOG_PROGRESS_CREATE_ACCOUNT         = 0x1008;
        public static final int DIALOG_PROMPT_CREATE_ACCOUNT           = 0x1009;
        public static final int DIALOG_PROMPT_CREATE_ACCOUNT_ERROR     = 0x1010;
        public static final int DIALOG_PROMPT_EXPORT_ACCOUNT           = 0x1011;
        public static final int DIALOG_PROMPT_EXPORT_ACCOUNT_SUCCESS   = 0x1012;
        public static final int DIALOG_PROMPT_EXPORT_ACCOUNT_FAILED    = 0x1013;
        public static final int DIALOG_PROMPT_IMPORT_ACCOUNT1          = 0x1012;
        public static final int DIALOG_PROMPT_IMPORT_ACCOUNT2          = 0x1013;
        public static final int DIALOG_PROMPT_IMPORT_ACCOUNT_ERROR     = 0x1014;
        public static final int DIALOG_PROMPT_DELETE_ACCOUNT           = 0x1015;
        public static final int DIALOG_PROMPT_DELETE_ACCOUNT_SUCCESS   = 0x1016;
        public static final int DIALOG_PROMPT_DELETE_ACCOUNT_FAILED    = 0x1017;
        public static final int DIALOG_PROGRESS_QRCODE_RECOGNITION     = 0x1018;
        public static final int DIALOG_PROMPT_QRCODE_RECOGNITION_ERROR = 0x1019;
        public static final int DIALOG_PROMPT_GET_QRCODE_BITMAP_ERROR  = 0x1020;
        public static final int DIALOG_PROMPT_FILE_MANAGER_ERROR       = 0x1021;
        public static final int DIALOG_PROMPT_ACCOUNT_INFO_ERROR       = 0x1022;
        public static final int CREATE_ACCOUNT                         = 0x2000;
        public static final int QRCODE_RECOGNITION                     = 0x2001;
        public static final int QRCODE_RECOGNITION_ALBUM               = 0x2002;
        public static final int ACCOUNT_RENAME                         = 0x2003;
        public static final int FILE_MANAGER                           = 0x2004;
    }

    public final static class ResultCode {
        public static final int CREATE_ACCOUNT           = 0x3000;
        public static final int QRCODE_RECOGNITION       = 0x3001;
        public static final int QRCODE_RECOGNITION_ALBUM = 0x3002;
        public static final int ACCOUNT_RENAME           = 0x3003;
        public static final int DEKETE_ACCOUNT           = 0x3004;
    }

    public final static class Configuration {
        public static final String CONFIGURATION = "CONFIGURATION";
        public static final String KEY1          = "SplashActivity";
    }

    public final static class BundleKey {
        public static final String ENCRYPT_KEY    = "encrypt_key";
        public static final String WALLET_ACCOUNT = "wallet_account";
        public static final String QRCODE_RESULT  = "qrcode_result";
    }

    public static class CustomProgressBarStatus {
        public static final int STATE_DEFAULT     = 101;
        public static final int STATE_DOWNLOADING = 102;
        public static final int STATE_DOWNLOADED  = 103;
    }

    public static class RecycleView {
        public static final int ADD_ACCOUNT = 0x7000;

        public static final int DATA_EMPTY     = 0x7001;
        public static final int DATA_LOADING   = 0x7002;
        public static final int DATA_NONE      = 0x7003;
        public static final int DATA_ERROR     = 0x7004;
        public static final int FOOTER_VIEW_ID = 0x7005;
        public static final int DATA_SIZE      = 10;
    }

    public static class StateCode {
        public static final int TEST_SUCCESS               = 0x8001;
        public static final int TEST_FAILED                = 0x8002;
        public static final int GENERATE_ECKEYPAIR_SUCCESS = 0x8003;
        public static final int GENERATE_ECKEYPAIR_FAILED  = 0x8004;
        public static final int EXPORT_ACCOUNT_SUCCESS     = 0x8005;
        public static final int EXPORT_ACCOUNT_FAILED      = 0x8006;
        public static final int DELETE_ACCOUNT_SUCCESS     = 0x8007;
        public static final int DELETE_ACCOUNT_FAILED      = 0x8008;
        public static final int QRCODE_RECOGNITION_SUCCESS = 0x8009;
        public static final int QRCODE_RECOGNITION_FAILED  = 0x8010;
        public static final int ACCOUNT_RENAME_SUCCESS     = 0x8011;
        public static final int ACCOUNT_RENAME_FAILED      = 0x8012;
        public static final int IMPORT_ACCOUNT_SUCCESS     = 0x8013;
        public static final int IMPORT_ACCOUNT_FAILED      = 0x8014;
    }
}
