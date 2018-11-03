package com.ty.nanfangscanner.presenter;

import android.content.Context;

import com.ty.nanfangscanner.bean.RegisterCheckInfo;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.net.HttpMethods;
import com.ty.nanfangscanner.net.ProgressSubscriber;
import com.ty.nanfangscanner.utils.FileUtils;
import com.ty.nanfangscanner.utils.Utils;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * @author TY on 2018/11/3.
 */
public class RegisterPresenter {

    RegisterUiInterface uiInterface;

    RegisterModelInterface registerModel = new RegisterModelInterfaceImpl();

    public RegisterPresenter(RegisterUiInterface uiInterface) {
        this.uiInterface = uiInterface;
    }

    private void doRegister(Context context,RequestBody requestBody,String authorization){


        HttpMethods.getInstance().doRegister(new ProgressSubscriber<List<RegisterCheckInfo>>(context, true) {
            @Override
            public void onNext(List<RegisterCheckInfo> registerCheckInfos) {
                super.onNext(registerCheckInfos);
                //设置日期格式 "yyyy-MM-dd HH:mm:ss"
                SimpleDateFormat df = new SimpleDateFormat(Utils.DATE_SIMPLE_H_M_S);
                // new Date()为获取当前系统时间
                String commitTime = df.format(new Date());
                if (registerCheckInfos != null) {
                    List<RegisterCheckInfo> successList = new ArrayList<>();
                    List<RegisterCheckInfo> failList = new ArrayList<>();
                    //存放key:产品名称,value:产品数量
                    Map<String, Integer> productMap = new HashMap<>();
                    List<String> productNameList = new ArrayList<>();
                    for (RegisterCheckInfo info : registerCheckInfos) {
                        String productName = info.getProductName();
                        if (info.getStatus() == 1) {
                            successList.add(info);
                        } else {
                            failList.add(info);
                        }
                        // 初始化 productMap
                        if (productNameList.contains(productName)) {
                            int newNum = productMap.get(productName) + 1;
                            productMap.put(productName, newNum);
                        } else {
                            productNameList.add(productName);
                            productMap.put(productName, 1);
                        }
                    }
                    //sectionNumberList.clear();
                    //adapter.notifyDataSetChanged();
                    //删除缓存文件
                    FileUtils.deleteFoder(new File(ConstantUtil.FILE_DIR + ConstantUtil.REGISTER_CACHE + ".txt"));

                    uiInterface.showCommitSuccessDialog(commitTime, successList, failList, productMap);

                }
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                String message;
                if (e.getCode() == 401) {
                    message = "权限拒绝，请联系管理员";
                } else if (e.getCode() == 1009) {
                    message = "当前网络不佳，请前往网络较好的场所重新发起提交！";
                } else {
                    message = e.getCode() + ":" + e.getMessage();
                }
                // UIUtils.showToast(message);
                uiInterface.showCommitFailDialog(message);
            }
        }, authorization, requestBody, ConstantUtil.COMPANY_CODE, ConstantUtil.TERMINALLID);

    }


}
