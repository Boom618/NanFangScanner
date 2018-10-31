package com.ty.nanfangscanner;

import com.ty.nanfangscanner.utils.TimeUtil;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
       // 2018-07-25T04:29:15
     //   2018-07-25T04:23:56
        String curretTime = TimeUtil.getCurretTime();
        System.out.println("TAG:::"+curretTime);
        String string="2018-08-23 12:14";
        int timeInterval = TimeUtil.getTimeInterval(curretTime, string);
        System.out.println("时间间隔==="+timeInterval);
    }
}