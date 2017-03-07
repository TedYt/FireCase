package com.hytera.fcls.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hytera.fcls.DataUtil;
import com.hytera.fcls.IFireCasePop;
import com.hytera.fcls.R;
import com.hytera.fcls.presenter.FireCasePopPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tim on 17/3/6.
 */
public class FireCasePopActivity extends Activity implements IFireCasePop {

    @BindView(R.id.fire_case_info)
    protected TextView fireCaseInfo;
    @BindView(R.id.fire_case_copy)
    protected Button fireCsaeCopy;
    @BindView(R.id.fire_case_no_copy)
    protected Button fireCaseNoCopy;

    private FireCasePopPresenter casePopPresenter;

    private Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 点击activity（dialog属性）外面，不退出activity
        setFinishOnTouchOutside(false);

        setContentView(R.layout.fire_case_pop_layout);
        ButterKnife.bind(this);

        casePopPresenter = new FireCasePopPresenter(this, this);
        Intent intent = getIntent();
        data = intent.getBundleExtra("data");
        extraData(data);
    }

    private void extraData(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        if (bundle != null) {
            sb.append(bundle.getStringArrayList(DataUtil.EXTRA_FIRE_LEVERL) + "\n")
                    .append(bundle.getString(DataUtil.EXTRA_FIRE_DEPR) + "\n")
                    .append(bundle.getString(DataUtil.EXTRA_FIRE_DESC) + "\n");
        } else {
            sb.append("三级" + "\n"
                    + "宝安区福永大洋田中粮福安机器人产业园15栋4楼被困电梯13380333115" + "\n"
                    + "新安中队");
        }
        fireCaseInfo.setText(sb.toString());
    }

    @OnClick({R.id.fire_case_no_copy, R.id.fire_case_copy})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fire_case_copy:
                casePopPresenter.copyCase();
                break;
            case R.id.fire_case_no_copy:
                casePopPresenter.rejectCopy();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this, getString(R.string.fire_case_pop_hint), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void showMainActivity() {
        // 进入App的主界面
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);

        finish();
    }


}
