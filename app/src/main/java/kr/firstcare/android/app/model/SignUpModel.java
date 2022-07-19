package kr.firstcare.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

/**
 * ClassName            SignUpModel
 * Created by JSky on   2020-07-08
 * <p>
 * Description          회원가입
 */

@Data
public class SignUpModel implements Serializable {
    @SerializedName("vteacher_id")
    @Expose
    public String vteacherId;
    @SerializedName("userpw")
    @Expose
    public String userPw;
    @SerializedName("vteacher_name")
    @Expose
    public String vteacherName;
    @SerializedName("hp")
    @Expose
    public String hp;
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
    @SerializedName("withdraw_id")
    @Expose
    public String withdrawId;
    @SerializedName("withdraw_ip")
    @Expose
    public String withdrawIp;
    @SerializedName("withdraw_date")
    @Expose
    public String withdrawDate;
    @SerializedName("withdraw_memo")
    @Expose
    public String withdrawMemo;
    @SerializedName("add_date")
    @Expose
    public String addDate;
    @SerializedName("add_id")
    @Expose
    public String addId;
    @SerializedName("add_ip")
    @Expose
    public String addIp;
    @SerializedName("select_agency_idx")
    @Expose
    public String[] selectAgencyIdx;


}
