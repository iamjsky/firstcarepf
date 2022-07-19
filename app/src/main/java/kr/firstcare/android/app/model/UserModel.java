package kr.firstcare.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName            UserModel
 * Created by JSky on   2020-06-17
 * <p>
 * Description          유저 정보
 */

@Data
public class UserModel implements Serializable {
    @SerializedName("idx")
    @Expose
    public Integer idx;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("vteacher_id")
    @Expose
    public String vteacherId;
    @SerializedName("userpw")
    @Expose
    public String userpw;
    @SerializedName("hp")
    @Expose
    public String hp;
    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("vteacher_name")
    @Expose
    public String vteacherName;
    @SerializedName("google_token")
    @Expose
    public String googleToken;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("email_flag")
    @Expose
    public String emailFlag;
    @SerializedName("sms_flag")
    @Expose
    public String smsFlag;
    @SerializedName("sex_flag")
    @Expose
    public String sexFlag;
    @SerializedName("zipcode")
    @Expose
    public String zipcode;
    @SerializedName("addr")
    @Expose
    public String addr;
    @SerializedName("addr_detail")
    @Expose
    public String addrDetail;
    @SerializedName("photo")
    @Expose
    public String photo;
    @SerializedName("user_code")
    @Expose
    public String userCode;
    @SerializedName("can_i_login")
    @Expose
    public String canILogin;
    @SerializedName("withdraw_flag")
    @Expose
    public String withdrawFlag;
    @SerializedName("withdraw_id")
    @Expose
    public String withdrawId;
    @SerializedName("withdraw_date")
    @Expose
    public String withdrawDate;
    @SerializedName("withdraw_memo")
    @Expose
    public String withdrawMemo;
    @SerializedName("del_flag")
    @Expose
    public String delFlag;
}
