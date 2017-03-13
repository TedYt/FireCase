package com.hytera.fcls.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.hytera.fcls.DataUtil;
import com.hytera.fcls.R;
import com.hytera.fcls.bean.FireCaseBean;

import butterknife.BindView;

/**
 * Created by Tim on 17/3/13.
 */

/**
 * 警情单
 */
public class FireCaseTable extends Activity {

    @BindView(R.id.fire_case_detail_anjian_bianhao)
    protected TextView anjian_bianhao;
    @BindView(R.id.fire_case_detail_baojing_time)
    protected TextView baojing_time;
    @BindView(R.id.fire_case_detail_xiada_time)
    protected TextView xiada_time;
    @BindView(R.id.fire_case_detail_jieshou_time)
    protected TextView jieshou_time;
    @BindView(R.id.fire_case_detail_xingzheng_quxian)
    protected TextView xingzheng_quxian;
    @BindView(R.id.fire_case_detail_zhuguan_zhongdui)
    protected TextView zhuguan_zhongdui;
    @BindView(R.id.fire_case_detail_baojing_dianhua)
    protected TextView baojing_dianhua;
    @BindView(R.id.fire_case_detail_lianxi_dianhua)
    protected TextView lianxi_dianhua;
    @BindView(R.id.fire_case_detail_anfa_dizhi)
    protected TextView anfa_dizhi;
    @BindView(R.id.fire_case_detail_chuli_duixiang)
    protected TextView chuli_duixiang;
    @BindView(R.id.fire_case_detail_chuli_fangfa)
    protected TextView chuli_fangfa;
    @BindView(R.id.fire_case_detail_cheliang_qingdan)
    protected TextView cheliang;
    @BindView(R.id.fire_case_detail_shuiyuan)
    protected TextView shuiyuan;
    @BindView(R.id.fire_case_detail_beizhu)
    protected TextView beizhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fire_case_detail_table);

        FireCaseBean bean = DataUtil.getFireCaseBean();
        if (bean != null){
            anjian_bianhao.setText(bean.getGuid());
            zhuguan_zhongdui.setText(bean.getCompDeptName());
            anfa_dizhi.setText(bean.getCaseDesc());
        }
    }
}
