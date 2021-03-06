package com.dasset.wallet.ui.activity.contract;

import com.dasset.wallet.base.presenter.BasePresenter;
import com.dasset.wallet.base.view.BaseView;
import com.dasset.wallet.model.TransactionRecords;

public interface AccountInfoContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void setSwipeRefreshLayout(boolean isRefresh);

        void showDeleteAccountPromptDialog();

        void loadTransactionRecords();

        void showAddressQRCodePromptDialog(byte[] data, String prompt);
    }

    interface Presenter extends BasePresenter {

        TransactionRecords getTransactionRecords();

        void exportAccount(String password);

        void renameAccount();

        void deleteAccount();

        void generateAddresQRCode();

        void save();

        void share();
    }
}
