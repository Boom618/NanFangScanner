package com.ty.nanfangscanner.presenter;

import com.ty.nanfangscanner.bean.RegisterCheckInfo;

import java.util.List;
import java.util.Map;

/**
 * @author TY on 2018/11/3.
 */
public interface RegisterUiInterface {

    /**
     * show loading
     */
    void showLoading();

    /**
     * hide loading
     */
    void hideLoading();

    /**
     * 提交 Success Dialog
     * @param commitTime
     * @param successList
     * @param failList
     * @param productMap
     */
    void showCommitSuccessDialog(String commitTime, List<RegisterCheckInfo> successList,
                                 List<RegisterCheckInfo> failList, Map<String, Integer> productMap);

    /**
     * 提交 失败 Dialog
     * @param message
     */
    void showCommitFailDialog(String message);
}
