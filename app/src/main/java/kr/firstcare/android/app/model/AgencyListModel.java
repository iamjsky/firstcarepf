package kr.firstcare.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName            AgencyListModel
 * Created by JSky on   2020-06-16
 * <p>
 * Description          소속기관 리스트
 */

@Data
public class AgencyListModel implements Serializable {


    @SerializedName("idx")
    @Expose
    public Integer idx;
    @SerializedName("sub_userid")
    @Expose
    public String subUserid;
    @SerializedName("sub_userpw")
    @Expose
    public String subUserpw;
    @SerializedName("sub_username")
    @Expose
    public String subUsername;
    @SerializedName("sub_hp")
    @Expose
    public String subHp;
    @SerializedName("sub_agency_name")
    @Expose
    public String subAgencyName;
    @SerializedName("sub_agency_number")
    @Expose
    public String subAgencyNumber;
    @SerializedName("sub_c_name")
    @Expose
    public String subCName;
    @SerializedName("sub_c_number")
    @Expose
    public String subCNumber;
    @SerializedName("sub_zipcode")
    @Expose
    public String subZipcode;
    @SerializedName("sub_addr")
    @Expose
    public String subAddr;
    @SerializedName("sub_addr_detail")
    @Expose
    public String subAddrDetail;
    @SerializedName("sub_tel")
    @Expose
    public String subTel;
    @SerializedName("clerk_name")
    @Expose
    public String clerkName;
    @SerializedName("sub_email")
    @Expose
    public String subEmail;
    @SerializedName("sub_email_flag")
    @Expose
    public String subEmailFlag;
    @SerializedName("sub_fax")
    @Expose
    public String subFax;
    @SerializedName("sub_img")
    @Expose
    public String subImg;
    @SerializedName("quality_evaluation")
    @Expose
    public String qualityEvaluation;
    @SerializedName("withdraw_flag")
    @Expose
    public String withdrawFlag;
    @SerializedName("can_i_login")
    @Expose
    public String canILogin;
    @SerializedName("login_fail_count")
    @Expose
    public String loginFailCount;
    @SerializedName("add_id")
    @Expose
    public String addId;
    @SerializedName("add_ip")
    @Expose
    public String addIp;
    @SerializedName("add_date")
    @Expose
    public String addDate;
    @SerializedName("edit_id")
    @Expose
    public String editId;
    @SerializedName("edit_ip")
    @Expose
    public String editIp;
    @SerializedName("edit_date")
    @Expose
    public String editDate;
    @SerializedName("del_flag")
    @Expose
    public String delFlag;
    @SerializedName("del_id")
    @Expose
    public String delId;
    @SerializedName("del_ip")
    @Expose
    public String delIp;
    @SerializedName("del_date")
    @Expose
    public String delDate;



    public int selected = 0;
    public int position = -1;
}
