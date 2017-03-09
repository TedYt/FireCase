package com.hytera.fcls;

/**
 * Created by Tim on 17/2/25.
 */

public interface IMainAtv {

    void updateLocation(double latitude, double longitude);

    void showLogInMain(String s);

    void showFireCaseInfo(String levelDesc, String caseDesc, String s);
}
