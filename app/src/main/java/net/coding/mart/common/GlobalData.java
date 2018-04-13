package net.coding.mart.common;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by chenchao on 2017/1/17.
 * 放一些全局的对象
 * 这个类只能 注入生成，不能用 new，否则会有问题
 * 是 aa 库里面唯一的例外
 */

@SharedPref(value = SharedPref.Scope.APPLICATION_DEFAULT)
public interface GlobalData {

    @DefaultString("mart-enterprise")
    String enterpriseGK();

    @DefaultInt(5)
    int maxMulPayCount();

    @DefaultString("")
    String projectPublishPaymentDeadline();

    @DefaultString("")
    String projectPublishPaymentTip();

//    @DefaultString("")
//    String projectPublishPaymentTip2();

    @DefaultString("优惠价")
    String projectPublishPaymentDeadTitle();

    // 项目发布现价
    @DefaultString("9.9")
    String projectPublishPayment();

    // 项目发布费
    @DefaultInt(99)
    int projectPublishPaymentOriginal();

}

