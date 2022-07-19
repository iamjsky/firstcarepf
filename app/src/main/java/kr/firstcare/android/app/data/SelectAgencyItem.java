package kr.firstcare.android.app.data;

import android.view.ViewGroup;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName            SelectAgencyItem
 * Created by JSky on   2020-07-07
 * <p>
 * Description          선택한 소속기관 아이템
 */
@Data
public class SelectAgencyItem implements Serializable {

   public ViewGroup item;
   public String agencyName;

}
