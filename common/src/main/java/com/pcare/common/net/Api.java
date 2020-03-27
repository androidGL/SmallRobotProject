package com.pcare.common.net;


import com.pcare.common.entity.BPMEntity;
import com.pcare.common.entity.GlucoseEntity;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.entity.UserEntity;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
//    String BASEURL = "http://192.168.13.192:8080";//武祥成本地
//    String BASEURL = "http://192.168.2.181:8000";//张宇阔本地
    String BASEURL = "http://192.168.2.180:8000";//服务器

    //header中添加 URL_KEY，表示这个请求是需要替换BaseUrl的
    String URL_KEY = "URL_KEY";
    //header中的value值，用于区分需要替换的BaseUrl是哪一个
    String URL_VALUE_SECOND = "URL_VALUE_SECOND";

    //人脸识别的URL
    String URL_VALUE_FACE = "URL_VALUE_FACE";
    String FACEURL = "http://192.168.2.181:8000";

    //问诊的URL

    String URL_VALUE_QUESTION = "URL_VALUE_QUESTION";
    String QUESTIONURL = "http://192.168.2.180:8080";
    //天益本地问诊的URL
    String URL_VALUE_ASK = "URL_VALUE_ASK";
    String ASKURL = "http://192.168.2.219:8088";
    //沈新哲本地的语音合成
    String URL_VALUE_AUDIO  = "URL_VALUE_AUDIO";
    String AUDIOURL = "http://192.168.2.191:6666";

    //这儿添加Headers是为了修改BaseURL的，key用于识别是不是需要修改BaseURL，value用来识别需要修改哪个BaseURL
    //使用时只需要在网络请求前添加： RetrofitUrlManager.getInstance().putDomain(Api.URL_VALUE_SECOND,"https://new.address.com");
    @Headers({URL_KEY+":"+URL_VALUE_SECOND})
    @POST("login")
    Single<NetResponse<UserEntity>> login(@Query("userName") String key);

    @GET("novelSearchApi")
    Observable<ResponseBody> testNet(@Query("userName") String ip);//测试+Rxjava

    /**
     * 用户相关
     */
    //1，判断重复
    @POST("users/is_user_unique/")
    @FormUrlEncoded
    Single<NetResponse> varifyUserName(@Field("item") JSONObject userName);
    //2,注册用户
    @POST("users/user_info_for_id/")
    @FormUrlEncoded
    Single<NetResponse> register(@Field("item") UserEntity userEntity);
    //3,修改用户
    @POST("users/modify/")
    @FormUrlEncoded
    Single<NetResponse<UserEntity>> modifyUser(@Field("item") UserEntity userEntity);
    //4，人脸注册
//    @Headers({URL_KEY+":"+URL_VALUE_FACE})
    @POST("face/detect64")
    @FormUrlEncoded
    Single<NetResponse> detectFace(@Field("usr_id") String userId,@Field("image_base64") String imageBase64);
    //5，人脸识别
//    @Headers({URL_KEY+":"+URL_VALUE_FACE})
    @POST("face/search64")
    @FormUrlEncoded
    Single<NetResponse> compareFace(@Field("image_base64") String imageBase64);


    /**
     * 血压相关
     */
    //1，保存血压
    @POST("asks/bpress/")
    @FormUrlEncoded
    Single<NetResponse> insertBPM(@Field("operate") String operate,@Field("item") BPMEntity entity);
    //2，查询血压
    @POST("asks/bpress/")
    @FormUrlEncoded
    Single<NetResponse> getBPMList2(@Field("operate") String operate);//operate传参query
    //3，查询血压
    @POST("asks/bpress/")
    @FormUrlEncoded
    Single<NetResponse> getBPMList(@Field("operate") String operate,@Field("item") JSONObject item);//operate传参bpress_query_user_id
    //4，删除血压
    @POST("asks/bpress/")
    @FormUrlEncoded
    Single<NetResponse> deleteBPM(@Field("operate") String operate,@Field("item") String id);//operate传参delete


    /**
     * 血糖相关
     */
    //1,保存血糖
    @POST("asks/glu/")
    @FormUrlEncoded
    Single<NetResponse> insertGLU(@Field("operate") String operate,@Field("item") GlucoseEntity entity);
    //2,查询血糖
    @POST("asks/glu/")
    @FormUrlEncoded
    Single<NetResponse> getGLUList2(@Field("operate") String operate,@Field("item") JSONObject object);//operate传参query
    //3,查询血糖
    @POST("asks/glu/")
    @FormUrlEncoded
    Single<NetResponse> getGLUList(@Field("operate") String operate,@Field("item") JSONObject object);//operate传参glu_query_user_id
    //4，删除血糖
    @POST("asks/glu/")
    @FormUrlEncoded
    Single<NetResponse> deleteGLU(@Field("operate") String operate,@Field("item") JSONObject object);//operate传参delete


    /**
     * 问诊相关
     */
    String WEB_GUO = "GUO",WEB_MEDICINE = "MEDICINE",WEB_DIAGNOSE = "DIAGNOSE",WEB_NCP = "NCP";
    String guoURL="https://www.zydsoft.cn/lungo/index.html?isver=27816";//国医堂中医问诊
    String medicineURL = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5e34f3a5b60c485208ab1743";//西医智能问药
    String diagnoseURL = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5e34f3999ea2ea369217e94a";//西医智能自诊
    String ncpURL = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5e4e43a69ea2ea68865eaa82";//西医智能自诊

    //问诊问题
    @Headers({URL_KEY+":"+URL_VALUE_QUESTION})
    @POST("asks/")
    @FormUrlEncoded
    Single<ResponseBody> getAnswer(@Field("question") String question);

    @Headers({URL_KEY+":"+URL_VALUE_ASK})
    @POST("asks/")
    @FormUrlEncoded
    Single<ResponseBody> ask(@Field("query") String question);

    @POST("search64")
    Single<NetResponse> getUserList(@Query("image_base64") String imageBase64) ;
    @Headers({URL_KEY+":"+URL_VALUE_AUDIO})
    @GET("tts")
    Single<ResponseBody> playAudio(@Query("text") String text);

}
