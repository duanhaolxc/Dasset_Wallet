package com.dasset.wallet.core.db.facade.implement;

import android.database.sqlite.SQLiteOpenHelper;

import com.dasset.wallet.core.db.base.IDb;
import com.dasset.wallet.core.db.facade.wrapper.HDAccountAddressProviderWrapper;
import com.dasset.wallet.core.db.helper.TxDataBaseHelper;

public class HDAccountAddressProvider extends HDAccountAddressProviderWrapper {

    private static HDAccountAddressProvider blockProvider;
    private SQLiteOpenHelper sqLiteOpenHelper;

    private HDAccountAddressProvider(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    public static synchronized HDAccountAddressProvider getInstance() {
        if (blockProvider == null) {
            blockProvider = new HDAccountAddressProvider(TxDataBaseHelper.getInstance());
        }
        return blockProvider;
    }

    public static void releaseInstance() {
        if (blockProvider != null) {
            blockProvider = null;
        }
    }

    @Override
    public IDb getReadDb() {
        return new Db(this.sqLiteOpenHelper.getReadableDatabase());
    }

    @Override
    public IDb getWriteDb() {
        return new Db(this.sqLiteOpenHelper.getWritableDatabase());
    }
}
