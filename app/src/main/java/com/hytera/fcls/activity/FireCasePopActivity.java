package com.hytera.fcls.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * 新警情来的时候，弹出的界面
 */
public class FireCasePopActivity extends BaseActivity implements IFireCasePop {

    public static final String TAG = "y20650" + "FireCasePop";

    @BindView(R.id.fire_case_info)
    protected TextView fireCaseInfo;
    @BindView(R.id.fire_case_copy)
    protected Button fireCsaeCopy;
    @BindView(R.id.fire_case_no_copy)
    protected Button fireCaseNoCopy;

    @BindView(R.id.pop_case_desc)
    protected TextView pop_case_desc;
    @BindView(R.id.pop_case_dept)
    protected TextView pop_case_dept;
    @BindView(R.id.pop_case_level)
    protected TextView pop_case_level;
    @BindView(R.id.level_icon_1)
    protected ImageView level_icon_1;
    @BindView(R.id.level_icon_2)
    protected ImageView level_icon_2;
    @BindView(R.id.level_icon_3)
    protected ImageView level_icon_3;
    @BindView(R.id.level_icon_4)
    protected ImageView level_icon_4;
    @BindView(R.id.level_icon_5)
    protected ImageView level_icon_5;

    private FireCasePopPresenter casePopPresenter;

    private Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 点击activity（dialog属性）外面，不退出activity
        setFinishOnTouchOutside(false);

        setContentView(R.layout.fire_case_pop_layout);
        ButterKnife.bind(this);

        initLevelIcon();

        casePopPresenter = new FireCasePopPresenter(this, this);
        Intent intent = getIntent();
        data = intent.getBundleExtra("data");
        extraData(data);
    }

    private void initLevelIcon() {
        level_icon_1.setVisibility(View.GONE);
        level_icon_2.setVisibility(View.GONE);
        level_icon_3.setVisibility(View.GONE);
        level_icon_4.setVisibility(View.GONE);
        level_icon_5.setVisibility(View.GONE);
    }

    private void extraData(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        if (bundle != null) {
            String levelDesc = DataUtil.getLevelDesc(bundle.getString(DataUtil.EXTRA_FIRE_LEVERL));
            pop_case_desc.setText(bundle.getString(DataUtil.EXTRA_FIRE_DESC));
            pop_case_dept.setText(bundle.getString(DataUtil.EXTRA_FIRE_DEPR));
            pop_case_level.setText(levelDesc);
            showFireLevelIcon(bundle.getString(DataUtil.EXTRA_FIRE_LEVERL));
            /*sb.append(levelDesc + "\n")
                    .append(bundle.getString(DataUtil.EXTRA_FIRE_DEPR) + "\n")
                    .append(bundle.getString(DataUtil.EXTRA_FIRE_DESC) + "\n");*/
        } else {
            sb.append("三级" + "\n"
                    + "宝安区福永大洋田中粮福安机器人产业园15栋4楼被困电梯13380333115" + "\n"
                    + "新安中队");
        }
        fireCaseInfo.setText(sb.toString());
    }

    private void showFireLevelIcon(String s) {
        int level = Integer.valueOf(s);
        switch (level){
            case 1:
                level_icon_1.setVisibility(View.VISIBLE);
                break;
            case 2:
                level_icon_1.setVisibility(View.VISIBLE);
                level_icon_2.setVisibility(View.VISIBLE);
                break;
            case 3:
                level_icon_1.setVisibility(View.VISIBLE);
                level_icon_2.setVisibility(View.VISIBLE);
                level_icon_3.setVisibility(View.VISIBLE);
                break;
            case 4:
                level_icon_1.setVisibility(View.VISIBLE);
                level_icon_2.setVisibility(View.VISIBLE);
                level_icon_3.setVisibility(View.VISIBLE);
                level_icon_4.setVisibility(View.VISIBLE);
                break;
            case 5:
                level_icon_1.setVisibility(View.VISIBLE);
                level_icon_2.setVisibility(View.VISIBLE);
                level_icon_3.setVisibility(View.VISIBLE);
                level_icon_4.setVisibility(View.VISIBLE);
                level_icon_5.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.fire_case_no_copy, R.id.fire_case_copy})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fire_case_copy:
                casePopPresenter.acceptCase();
                break;
            case R.id.fire_case_no_copy:
                casePopPresenter.rejectCase();
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
        intent.putExtra("fire_case_data", data);
        startActivity(intent);

        finish();
    }


}
