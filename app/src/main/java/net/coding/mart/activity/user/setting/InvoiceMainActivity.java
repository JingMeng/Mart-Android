package net.coding.mart.activity.user.setting;

import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.widget.ListItem1;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_invoice_main)
public class InvoiceMainActivity extends BackActivity {

    @ViewById
    TextView invoiceAmout, textTip;

    @ViewById
    ListItem1 invoicing;

    @AfterViews
    void initInvoiceMainActivity() {
        Global.addLinkCustomerService(this, textTip);
        invoicing.setTextColor(0xFF999999);

        Network.getRetrofit(this)
                .getInvoiceAmout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BigDecimal>(this) {
                    @Override
                    public void onSuccess(BigDecimal data) {
                        super.onSuccess(data);

                        invoiceAmout.setText(data.toString());
                    }

                });
    }

}
