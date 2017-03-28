package com.hytera.fcls;

import com.amap.api.navi.model.NaviLatLng;
import com.google.gson.Gson;
import com.hytera.fcls.bean.LoginResponseBean;

/**
 * Created by Tim on 17/3/10.
 * <p>
 * 测试数据类。不能从服务器获得数据的时候，就用测试数据，
 * 保证测试能顺利往下进行
 */

public class TestClass {

    public static final String testCase =
            "{\"acceptDeptId\":\"BFFC553C2024496D8DA981B71C86FC30\",\"answeringTime\":1489044407491," +
                    "\"callingTime\":1489044407491,\"caseDefine\":\"DIC007001\"," +
                    "\"caseDesc\":\"宝安区福永大洋田中粮福安机器人产业园15栋4楼被困电梯13380333115\"," +
                    "\"caseDispatchState\":\"2\",\"caseLevel\":\"1\",\"caseState\":\"DIC001002\"," +
                    "\"caseTime\":1489044407491,\"caseType\":\"DIC008004\",\"compDeptName\":\"福永中队\"," +
                    "\"contactNO\":\"\",\"createTime\":1489044407491,\"delFlag\":\"0\"," +
                    "\"dispatchDeptId\":\"BFFC553C2024496D8DA981B71C86FC30\"," +
                    "\"dispatchRecord\":" +
                    "{\"biztype\":\"1\",\"caseid\":\"20170309152647489\",\"createtime\":1489044407587," +
                    "\"createuser\":\"system\",\"delflag\":\"0\",\"dispatcher\":\"system\",\"dispatchorgan\":\"system\"," +
                    "\"dispatchorglevel\":\"1\",\"dispatchway\":\"0\",\"guid\":\"352f0247-c10b-452b-b831-14b31916cca0\"," +
                    "\"policetype\":\"4\",\"receiveno\":\"BFFC553C2024496D8DA981B71C86FC30\",\"receiver\":\"福永中队\"," +
                    "\"sourcetype\":\"3\"},\"guid\":\"20170309152647489\",\"important\":false,\"manualPot\":false," +
                    "\"mapx\":113.801231,\"mapy\":22.69247," +
                    "\"orgInfo\":{\"busessTypeStr\":\"\",\"contact\":\"吴晓斌\",\"contactno\":\"13825269817\"," +
                    "\"createTime\":1488544302000,\"createUser\":\"admin\",\"enableFlag\":1,\"flag\":0," +
                    "\"guid\":\"BFFC553C2024496D8DA981B71C86FC30\",\"latitude\":22.681493,\"longitude\":113.821785," +
                    "\"orgIdentifier\":\"000-006\",\"orgName\":\"福永中队\",\"orgType\":\"1\",\"orgTypeStr\":\"中队\"," +
                    "\"parentOrgGuid\":\"1CDFAE9460CB4F8E81435F572E09F9D2\",\"seq\":5,\"updateTime\":1488605457000," +
                    "\"updateUser\":\"ycs\"},\"processDeptId\":\"BFFC553C2024496D8DA981B71C86FC30\",\"repeat\":false," +
                    "\"sendAllMsg\":false,\"updateTime\":1489044407491}";


    public static LoginResponseBean getTestLoginBean() {
        /**
         * {"msg":"登陆成功！","user":{"userCode":"baoan","staffName":"于大宝","orgGuid":"A86681A696D7451E8AB06863A533C253","orgIdentifier":"000-002","orgName":"宝安中队","orgType":"1","loginTime":"2017-03-16 09:18:16","ip":"192.168.26.20","token":"12316307B1A24A1EA7E8E7EDF54B0D5E"},"key":"1"}
         */
        Gson gson = new Gson();
        String response = "{\"msg\":\"登陆成功！\",\"user\":{\"userCode\":\"baoan\",\"staffName\":\"于大宝\",\"orgGuid\":\"A86681A696D7451E8AB06863A533C253\",\"orgIdentifier\":\"000-002\",\"orgName\":\"宝安中队\",\"orgType\":\"1\",\"loginTime\":\"2017-03-16 09:18:16\",\"ip\":\"192.168.26.20\",\"token\":\"12316307B1A24A1EA7E8E7EDF54B0D5E\"},\"key\":\"1\"}";
        return gson.fromJson(response, LoginResponseBean.class);
    }

    /**
     * 模拟出发位置
     * @return
     */
    public static NaviLatLng getStartNaviLatLng() {
        NaviLatLng mStartLatlng1 = new NaviLatLng(22.534809, 113.943966);
        return mStartLatlng1;
    }

    /**
     * 模拟到达位置
     * @return
     */
    public static NaviLatLng getEndNaviLatLng() {
        NaviLatLng mEndLatlng1 = new NaviLatLng(22.681493, 113.821785);
        return mEndLatlng1;
    }

}
