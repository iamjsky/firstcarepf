package kr.firstcare.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName            NoticeListModel
 * Created by JSky on   2020-06-16
 * <p>
 * Description          공지사항 게시물 리스트 (사용중)
 */

@Data
public class BoardListModel implements Serializable {

    @SerializedName("idx")
    @Expose
    public Integer idx;
    @SerializedName("board_id")
    @Expose
    public String boardId;
    @SerializedName("group_idx")
    @Expose
    public Integer groupIdx;
    @SerializedName("thread")
    @Expose
    public Integer thread;
    @SerializedName("group_sort")
    @Expose
    public Integer groupSort;
    @SerializedName("parent_idx")
    @Expose
    public Integer parentIdx;
    @SerializedName("subject")
    @Expose
    public String subject;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("hp")
    @Expose
    public String hp;
    @SerializedName("tel")
    @Expose
    public String tel;
    @SerializedName("hit_count")
    @Expose
    public Integer hitCount;
    @SerializedName("secret_flag")
    @Expose
    public String secretFlag;
    @SerializedName("password")
    @Expose
    public Object password;
    @SerializedName("editor_flag")
    @Expose
    public String editorFlag;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("reply_mail_yn")
    @Expose
    public String replyMailYn;
    @SerializedName("comment_count")
    @Expose
    public Integer commentCount;
    @SerializedName("notice_flag")
    @Expose
    public String noticeFlag;
    @SerializedName("s_date")
    @Expose
    public String sDate;
    @SerializedName("e_date")
    @Expose
    public String eDate;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("asso_name")
    @Expose
    public String assoName;
    @SerializedName("asso_size")
    @Expose
    public String assoSize;
    @SerializedName("sound_type")
    @Expose
    public String soundType;
    @SerializedName("answer_yn")
    @Expose
    public String answerYn;
    @SerializedName("occur_date")
    @Expose
    public String occurDate;
    @SerializedName("by_admin")
    @Expose
    public String byAdmin;
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
    @SerializedName("category")
    @Expose
    public String category;






}
