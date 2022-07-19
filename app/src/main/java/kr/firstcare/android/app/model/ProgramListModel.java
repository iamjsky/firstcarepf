package kr.firstcare.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName            ProgramListModel
 * Created by JSky on   2020-06-17
 * <p>
 * Description          서비스 리스트
 */
@Data
public class ProgramListModel implements Serializable {

    @SerializedName("idx")
    @Expose
    public Integer idx;
    @SerializedName("service_code")
    @Expose
    public String serviceCode;
    @SerializedName("year")
    @Expose
    public String year;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("add_date")
    @Expose
    public String addDate;
    @SerializedName("add_id")
    @Expose
    public String addId;
    @SerializedName("add_ip")
    @Expose
    public String addIp;
    @SerializedName("edit_date")
    @Expose
    public String editDate;
    @SerializedName("edit_id")
    @Expose
    public String editId;
    @SerializedName("edit_ip")
    @Expose
    public String editIp;
    @SerializedName("del_flag")
    @Expose
    public String delFlag;
    @SerializedName("del_date")
    @Expose
    public String delDate;
    @SerializedName("del_id")
    @Expose
    public String delId;
    @SerializedName("del_ip")
    @Expose
    public String delIp;
    @SerializedName("vteacher_idx")
    @Expose
    public Integer vteacherIdx;
    @SerializedName("sub_agency_idx")
    @Expose
    public Integer subAgencyIdx;
    @SerializedName("program_idx")
    @Expose
    public Integer programIdx;

}