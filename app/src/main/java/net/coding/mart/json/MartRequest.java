package net.coding.mart.json;

import net.coding.mart.common.LoginBackground;
import net.coding.mart.json.developer.AliPayVO;
import net.coding.mart.json.developer.CalculateResult;
import net.coding.mart.json.developer.FunctionResult;
import net.coding.mart.json.developer.Functions;
import net.coding.mart.json.developer.ShareVO;
import net.coding.mart.json.developer.WeixinPayVO;
import net.coding.mart.json.enterprise.Attachment;
import net.coding.mart.json.enterprise.EnterpriseCertificate;
import net.coding.mart.json.global.Settings;
import net.coding.mart.json.login.Captcha;
import net.coding.mart.json.mart2.user.City;
import net.coding.mart.json.mart2.user.MartUser;
import net.coding.mart.json.mpay.AlipayRecharge;
import net.coding.mart.json.mpay.FreezeDynamiicPager;
import net.coding.mart.json.mpay.MPayAccount;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.mpay.OrderMapper;
import net.coding.mart.json.mpay.OrderPage;
import net.coding.mart.json.mpay.PayOrder;
import net.coding.mart.json.mpay.SingleWithdrawAccount;
import net.coding.mart.json.mpay.WeixinRecharge;
import net.coding.mart.json.mpay.WithdrawRequire;
import net.coding.mart.json.mpay.WithdrawResult;
import net.coding.mart.json.reward.BasicInfo;
import net.coding.mart.json.reward.IndustryPager;
import net.coding.mart.json.reward.JoinJobPage;
import net.coding.mart.json.reward.MulStageOrder;
import net.coding.mart.json.reward.Project;
import net.coding.mart.json.reward.Published;
import net.coding.mart.json.reward.PublishedPageResult;
import net.coding.mart.json.reward.RewardApply;
import net.coding.mart.json.reward.RewardDetail;
import net.coding.mart.json.reward.RewardDynamic;
import net.coding.mart.json.reward.SimplePublished;
import net.coding.mart.json.reward.project.ProjectPublish;
import net.coding.mart.json.reward.user.ApplyContact;
import net.coding.mart.json.reward.user.ApplyContactResult;
import net.coding.mart.json.reward.user.ApplyResume;
import net.coding.mart.json.user.JoinState;
import net.coding.mart.json.user.UserExtraProjectExp;
import net.coding.mart.json.user.UserExtraRole;
import net.coding.mart.json.user.exam.Exam;
import net.coding.mart.json.user.exam.ExamAnswer;
import net.coding.mart.json.user.identity.IdentityCheck;
import net.coding.mart.json.user.identity.IdentitySign;
import net.coding.mart.json.v2.V2ApplyPager;
import net.coding.mart.json.v2.V2Owners;
import net.coding.mart.json.v2.phase.Phases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chenchao on 16/3/18.
 * 码市网络请求
 */
public interface MartRequest {

    @GET("userinfo/user_roles")
    Observable<HttpResult<List<UserExtraRole>>> getUserRoles();

    @GET("userinfo/roles")
    Observable<HttpResult<List<UserExtraRole>>> getAllRoleTypes();

    @GET("userinfo/project-exp")
    Observable<HttpResult<List<UserExtraProjectExp>>> getUserProjectExp();

    // 设置 pageSize 没有用, 移动端只能返回 9 个
    // reward_list?high_paid=2 高回报项目
    @GET("reward/list?")
    Observable<HttpPageResult<SimplePublished>> getRewardPages(@Query("type") int type,
                                                               @Query("status") int status,
                                                               @Query("role_type_id") int role,
                                                               @Query("page") int page,
                                                               @Query("pageSize") int pageSize,
                                                               @Query("high_paid") int highPaid);

    @GET("reward/role/types")
    Observable<HttpResult<List<RoleType>>> getPublishRoleTypes();

    @GET("reward/list?high_paid=1")
    Observable<HttpPageResult<SimplePublished>> getHighPayReward();

    @GET("p/{id}")
    Observable<HttpResult<JobDetail>> getRewardDetail(@Path("id") int id);

    @GET("notification/unread/count")
    Observable<HttpResult<Integer>> getUnreadCount();

    @GET("quote/functions")
    Observable<HttpResult<Functions>> getFunctions();

    //阿里支付
    @FormUrlEncoded
    @POST("payment/app/charge")
    Observable<HttpResult<AliPayVO>> developerAlipay(@FieldMap Map<String, String> param);

    //微信支付
    @FormUrlEncoded
    @POST("payment/app/charge")
    Observable<HttpResult<WeixinPayVO>> payByWeixin(@FieldMap Map<String, String> param);

    @GET("quote/list")
    Observable<HttpResult<List<net.coding.mart.json.developer.Datum>>> getPriceList();

    @GET("quote/{id}")
    Observable<HttpResult<FunctionResult>> getFunctionResult(@Path("id") int id);


    @POST("quote/{id}/share")
    Observable<HttpResult<ShareVO>> getShareLink(@Path("id") int id);

    @FormUrlEncoded
    @POST("quote/pre-save")
    Observable<HttpResult<CalculateResult>> getCalResult(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("quote/{id}/update-pre-save")
    Observable<HttpResult<String>> saveCalResult(@Path("id") int id,
                                                 @Field("name") String name,
                                                 @Field("description") String description);

    @FormUrlEncoded
    @POST("project")
    Observable<Project> createReward(@FieldMap Map<String, String> param);

    @POST("project/publish/payment/{id}")
    Observable<Order> createRwardOrder(@Path("id") String id);

    @FormUrlEncoded
    @POST("userinfo/check_verify_code")
    Observable<SimpleHttpResult> checkPhoneVerify(@Field("mobile") String mobile, @Field("code") String code);

    @GET("rewards-preview")
    Observable<HttpResult<RewardsPreview>> getRewardsPreview();

    @FormUrlEncoded
    @POST("userinfo/send_verify_code")
    Observable<SimpleHttpResult> rewardSendMessage(@Field("phoneCountryCode") String country,
                                                   @Field("mobile") String mobile);

    @GET("banner/type/top")
    Observable<HttpResult<List<Banner>>> getBanners();

    @POST("userinfo/roles")
    Observable<SimpleHttpResult> addRoleType(@Query("role_ids[]") ArrayList<String> newType);

    @FormUrlEncoded
    @POST("userinfo/role")
    Observable<SimpleHttpResult> addRole(@Field("role_id") int roleId,
                                         @Query("skill_ids[]") ArrayList<String> ids,
                                         @Query("abilities") String abilities,
                                         @Field("good_at") String goodAt);

    @FormUrlEncoded
    @POST("userinfo/project-exp")
    Observable<BaseHttpResult> addProjectExp(@FieldMap Map<String, String> param, @Query("file_ids[]") ArrayList<String> files);

    @POST("userinfo/project-exp/del/{id}")
    Observable<SimpleHttpResult> deleteProjectExp(@Path("id") int id);

    @FormUrlEncoded
    @POST("joined/cancel")
    Observable<SimpleHttpResult> cancelJoin(@Field("id") int id);

    @GET("reward/{id}/detail")
    Observable<HttpResult<RewardDetail>> getRewardDetail2(@Path("id") int id);

    @GET("reward/{id}/detail/basic_info")
    Observable<HttpResult<BasicInfo>> getRewardDetailBasic(@Path("id") int id);

    @Deprecated
    // 旧接口
    @GET("reward/{id}")
    Observable<HttpResult<Published>> getPublishRewardDetail(@Path("id") int id);

    // 新接口
    @GET("project/{id}")
    Observable<ProjectPublish> getProjectPublishDetail(@Path("id") int id);

    @GET("project/{id}/phase")
    Observable<Phases> getProjectPhase(@Path("id") int id);

    @GET("project/{id}/owner")
    Observable<V2Owners> getProjectOwner(@Path("id") int id);

    @FormUrlEncoded
    @POST("stage/modify")
    Observable<SimpleHttpResult> stageSubmitFail(@Field("stageId") int id, @Field("file") String file);

    @FormUrlEncoded
    @POST("stage/handover")
    Observable<SimpleHttpResult> stageSubmit(@Field("stageId") int id, @Field("file") String file);

    @FormUrlEncoded
    @POST("stage/cancelhandover")
    Observable<SimpleHttpResult> stageSubmitCancel(@Field("stageId") int id);


    @GET("reward/activities/{id}")
    Observable<HttpPageResult<RewardDynamic>> getRewardDynamic(@Path("id") int id,
                                                               @Query("page") int page);

    @FormUrlEncoded
    @POST("userinfo/send-verify-email")
    Observable<BaseHttpResult> sendValidEmail(@Field("email") String email);

    @FormUrlEncoded
    @POST("userinfo/email")
    Observable<MartUser> modifyWorkEmail(@Field("email") String email,
                                         @Field("code") String code);

    @GET("published?pageSize=20")
    Observable<PublishedPageResult> getMyPublishList(@Query("page") int page);

    @GET("joined?pageSize=20")
    Observable<JoinJobPage> getMyJoinList(@Query("page") int page, @Query("status") String status);

    @FormUrlEncoded
    @POST("mpay/password")
    Observable<SimpleHttpResult> resetMPayPassword(@Field("newPassword") String password,
                                                   @Field("verifyCode") String code);

    @FormUrlEncoded
    @PUT("mpay/password")
    Observable<SimpleHttpResult> modifyPayPassword(@Field("oldPassword") String oldPassword,
                                                   @Field("newPassword") String newPassword);

    @GET("mpay/orders?size=10")
    Observable<OrderPage> getMPayOrderPage(@Query("page") int page,
                                           @Query("from") String time,
                                           @Query("type") List<String> types,
                                           @Query("status") List<String> status,
                                           @Query("types") List<String> oldTypes);

    @GET("mpay/account")
    Observable<MPayAccount> getMPayAccount();

    //    {"account": {"id": 2068,"userId": 7074,"name": "陈超","account": "cc191954@gmail.com","accountType": "Alipay","status": "Pending","createdAt": "1470300407808","updatedAt": "1470300407808","deletedAt": "-28800000"}}
    @FormUrlEncoded
    @POST("mpay/withdraw/account")
    Observable<SingleWithdrawAccount> setWithdrawAccount(@Field("name") String name,
                                                         @Field("account") String account);

    @GET("mpay/withdraw/account")
    Observable<SingleWithdrawAccount> getWithdrawAccount();

    @GET("mpay/withdraw/require")
    Observable<WithdrawRequire> getWithdrawRequire();

    /*
    account:
    accountType:Alipay
    name:
    accountId:1111
    price:1
    description:测试
    password:
     */
    @FormUrlEncoded
    @POST("mpay/withdraw")
    Observable<WithdrawResult> mpayWithDraw(@FieldMap Map<String, String> map);

    @GET("mpay/withdraw/{orderId}")
    Observable<WithdrawResult> orderDetail(@Path("orderId") String id);

    @FormUrlEncoded
    @POST("reward/{id}/prepayment")
    Observable<Order> createDepositOrder(@Path("id") int id, @Field("totalFee") String fee);

    // 新的支付
    @GET("mpay/payment/orders")
    Observable<PayOrder> createPayOrders(@Query("orderId") ArrayList<String> orders);

    @FormUrlEncoded
    @POST("mpay/deposit")
    Observable<AlipayRecharge> payByAlipay(@Field("price") String money,
                                           @Field("payments") ArrayList<String> payments,
                                           @Field("platform") String platform,
                                           @Field("service") String service);

    @FormUrlEncoded
    @POST("mpay/deposit")
    Observable<WeixinRecharge> payByWeixin(@Field("price") String money,
                                           @Field("payments") ArrayList<String> payments,
                                           @Field("platform") String platform,
                                           @Field("service") String service);

    @FormUrlEncoded
    @POST("mpay/payment/order/{id}")
    Observable<BaseHttpResult> payByMPay(@Path("id") String id, @Field("password") String password);

    @GET("mpay/order/{orderId}/status")
    Observable<HttpResult<String>> getOrderStatus(@Path("orderId") String orderId);

    @FormUrlEncoded
    @POST("mpay/deposit")
    Observable<AlipayRecharge> alipayRecharge(@Field("price") String money,
                                              @Field("platform") String platform,
                                              @Field("service") String service);

    @FormUrlEncoded
    @POST("mpay/deposit")
    Observable<WeixinRecharge> weixinRecharge(@Field("price") String money,
                                              @Field("platform") String platform,
                                              @Field("service") String service);

    @FormUrlEncoded
    @POST("developer/info")
    Observable<HttpResult<JoinState>> setJoinState(@Field("acceptNewRewardAllNotification") boolean accept,
                                                   @Field("freeTime") int freeTime,
                                                   @Field("rewardRole") int rewardRole);

    @GET("mpay/freeze/records?size=10")
    Observable<FreezeDynamiicPager> getFreezeDynamics(@Query("page") int page);

    @POST("mpay/stage/{id}/order")
    Observable<Order> getStageOrder(@Path("id") int stageId);

    @FormUrlEncoded
    @POST("mpay/stage/{id}/acceptance")
    Observable<BaseHttpResult> stageAccetpance(@Path("id") int stageId, @Field("password") String password);

    @FormUrlEncoded
    @POST("join")
    Observable<BaseHttpResult> joinReward(@Field("reward_id") int rewardId,
                                          @Field("message") String message,
                                          @Field("secret") int secret, // 永远传 1
                                          @Field("role_type") int roleType,
                                          @Field("roleIdArr[]") ArrayList<Integer> roleIdArr,
                                          @Field("projectIdArr[]") ArrayList<Integer> projectIdArr);

    @GET("reward/{id}/apply")
    Observable<HttpResult<RewardApply>> getRewardApply(@Path("id") int rewardId);

    @GET("reward/{rewardId}/apply/{applyId}/resume")
    Observable<ApplyResume> getApplyResume(@Path("rewardId") int rewardId,
                                           @Path("applyId") int applyId);

    @GET("apply/{id}/contact")
    Observable<ApplyContact> getApplyContact(@Path("id") int applyId);

    @POST("apply/{applyId}/reject")
    Observable<BaseHttpResult> applyReject(@Path("applyId") int applyId);


    @POST("apply/{applyId}/pass")
    Observable<BaseHttpResult> applyPass(@Path("applyId") int applyId);

    @GET("industry")
    Observable<IndustryPager> getRewardIndustry();

    @FormUrlEncoded
    @POST("cancel")
    Observable<BaseHttpResult> cancelReward(@Field("id") int id, @Field("reason") String reasion);

    @GET("reward/{id}/apply/contact/param")
    Observable<ApplyContactResult> getApplyContactParam(@Path("id") int rewardId);

    @POST("apply/{id}/order")
    Observable<Order> getApplyContactOrder(@Path("id") int applyId);

    @GET("app/survey")
    Observable<HttpResult<Exam>> getExam();

    @Headers("Content-Type: application/json")
    @POST("app/survey")
    Observable<HttpResult<ExamAnswer>> postExam(@Body Map<String, Map> answers);

    @GET("payment/charge_payed/{id}/check")
    Observable<SimpleHttpResult> checkOrderPayStatus(@Path("id") String orderId);

    @FormUrlEncoded
    @POST("user/identity")
    Observable<IdentityCheck> userIdentity(@Field("name") String name,
                                           @Field("identity") String idCard);

    @GET("user/identity/sign")
    Observable<IdentitySign> getIdentitySign();

    // 用开发宝支付单笔订单
    @FormUrlEncoded
    @POST("mpay/stage/multi/order")
    Observable<MulStageOrder> mulStageOrder(@Field("rewardId") int rewardId, @Field("roleId") int roleId);

    // 用开发宝支付多笔订单
    @FormUrlEncoded
    @POST("mpay/payment/order/multi")
    Observable<BaseHttpResult> payMulStageOrder(@Field("orderId") ArrayList<String> orders, @Field("password") String password);

    @GET("setting?code=max_multi_pay_size")
    Observable<HttpResult<Integer>> getMulPaySize();

    @GET("setting?code=mart_enterprise_gk")
    Observable<HttpResult<String>> getMartEnterpriseGK();

    @GET("settings")
    Observable<Settings> getGlobalSettings();

    @FormUrlEncoded
    @POST("user/free-time")
    Observable<MartUser> setFreeTime(@Field("freeTime") int freeTime,
                                     @Field("acceptNewRewardAllNotification") boolean notify);

    @GET("mpay/const/order/mapper")
    Observable<HttpResult<OrderMapper>> getOrderMapper();

    @GET("enterprise/certificate")
    Observable<EnterpriseCertificate> getEnterpriseCertificate();

    @FormUrlEncoded
    @POST("enterprise/certificate")
    Observable<EnterpriseCertificate> postEnterpriseCertificate(@Field("legalRepresentative") String legalRepresentative,
                                                                @Field("businessLicenceNo") String businessLicenceNo,
                                                                @Field("businessLicenceImg") int businessLicenceImg);

    @Multipart
    @POST("upload")
    Observable<HttpResult<Attachment>> postImage(@Part MultipartBody.Part body);


    @GET("enterprise/invoice/amount")
    Observable<HttpResult<BigDecimal>> getInvoiceAmout();

    @FormUrlEncoded
    @POST("login")
    Observable<MartUser> login(@Field("account") String account,
                               @Field("password") String password,
                               @Field("remember") boolean remember,
                               @Field("captcha") String captcha);

    @GET("account/captcha")
    Observable<Captcha> getCaptcha();

    @GET("user/current")
    Observable<MartUser> currentUser();

    @FormUrlEncoded
    @POST("userinfo")
    Observable<MartUser> modifyUser(@FieldMap Map<String, String> userInfo);

    @POST("notification/all/mark")
    Observable<SimpleHttpResult> markAllNotify();

    // type = { all, unread }
    @GET("notification/{type}")
    Observable<HttpPageResult<Notification>> getNotification(@Path("type") String type, @Query("page") int page, @Query("pageSize") int pageSize);

    @POST("notification/{id}/mark")
    Observable<BaseHttpResult> markNotification(@Path("id") int id);

    @GET("region")
    Observable<HttpResult<List<City>>> getCity(@Query("parent") int parent, @Query("level") int level);

    @GET("banner/app")
    Observable<HttpResult<List<LoginBackground.PhotoItem>>> getBanner();

    @FormUrlEncoded
    @POST("identity")
    Observable<BaseHttpResult> saveIdentity(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("register")
    Observable<MartUser> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("password")
    Observable<SimpleHttpResult> setPassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("account/verification-email")
    Observable<SimpleHttpResult> sendVerificationEmail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("account/verification-code/validate")
    Observable<BaseHttpResult> validateCodeV2(@Field("phone") String phone,
                                              @Field("countryCode") String countryCode,
                                              @Field("isoCode") String isoCode,
                                              @Field("verificationCode") String code,
                                              @Field("action") String action);

    // 发送手机短信验证码
    @FormUrlEncoded
    @POST("account/verification-code")
    Observable<BaseHttpResult> sendValidateCodeV2(@Field("phone") String phone,
                                                  @Field("countryCode") String countryCode,
                                                  @Field("isoCode") String isoCode);

    @GET("account/{gk}")
    Observable<SimpleHttpResult> gkNoUse(@Path("gk") String gk);

    @GET("account/{phone}")
    Observable<SimpleHttpResult> phoneNoUse(@Path("phone") String phone,
                                            @Query("countryCode") String countryCode,
                                            @Query("isoCode") String isoCode);

    //// 待删除
    @FormUrlEncoded
    @POST("userinfo/mobile")
    Observable<MartUser> modifyPhone(@Field("mobile") String phone,
                                     @Field("phoneCountryCode") String countryCode,
                                     @Field("country") String countryIso,
                                     @Field("code") String code);

    // 旧版的发送手机短信验证码
    @FormUrlEncoded
    @POST("userinfo/send_verify_code_with_country")
    Observable<BaseHttpResult> sendVerifyCode(@Field("mobile") String phone,
                                              @Field("phoneCountryCode") String countryCode);

    // 获取反馈的图形验证码
    @GET("feedback/captcha")
    Observable<Captcha> getFeedbackCaptcha();

    @FormUrlEncoded
    @POST("feedback")
    Observable<BaseHttpResult> feedback(@Field("user") String user,
                                        @Field("content") String content,
                                        @Field("contactName") String contactName,
                                        @Field("contactEmail") String contactEmail,
                                        @Field("captcha") String captcha,
                                        @Field("type") ArrayList<String> type);

    @GET("apply/project/{id}?size=500")
    Observable<V2ApplyPager> getAllApplys(@Path("id") int id);

    @POST("apply/{id}/marked")
    Observable<BaseHttpResult> markCoder(@Path("id") int id);
}

