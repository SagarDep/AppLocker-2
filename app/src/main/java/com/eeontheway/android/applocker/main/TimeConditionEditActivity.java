package com.eeontheway.android.applocker.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;

import com.eeontheway.android.applocker.R;
import com.eeontheway.android.applocker.lock.LockConfigManager;
import com.eeontheway.android.applocker.lock.TimeLockCondition;
import com.eeontheway.android.applocker.utils.SystemUtils;

import java.util.Calendar;

/**
 * 时间配置编辑Activity
 *
 * @author lishutong
 * @version v1.0
 * @Time 2016-2-8
 */
public class TimeConditionEditActivity extends AppCompatActivity {
    private static final String PARAM_EDIT_MODE = "editMode";
    private static final String PARAM_TIME_CONFIG = "timeConfig";

    private TimePicker tp_start_time;
    private TimePicker tp_end_time;
    private CheckBox cb_day1;
    private CheckBox cb_day2;
    private CheckBox cb_day3;
    private CheckBox cb_day4;
    private CheckBox cb_day5;
    private CheckBox cb_day6;
    private CheckBox cb_day7;
    private Button bt_ok;
    private Button bt_cancel;

    private LockConfigManager lockConfigManager;
    private boolean isEditMode;
    private TimeLockCondition timeLockCondition;

    /**
     * 启动界面，用于编辑时间配置
     * @param context 上下文
     */
    public static void start (Context context, int index) {
        Intent intent = new Intent(context, TimeConditionEditActivity.class);
        intent.putExtra(PARAM_EDIT_MODE, true);
        intent.putExtra(PARAM_TIME_CONFIG, index);
        context.startActivity(intent);
    }

    /**
     * 启动界面，用于创建新项
     * @param context 上下文
     */
    public static void start (Context context) {
        Intent intent = new Intent(context, TimeConditionEditActivity.class);
        intent.putExtra(PARAM_EDIT_MODE, false);
        context.startActivity(intent);
    }

    /**
     * Activity的onCreate回调
     *
     * @param savedInstanceState 之前保存的状态
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_config_edit);

        lockConfigManager = LockConfigManager.getInstance(this);

        // 读取配置参数
        isEditMode = getIntent().getBooleanExtra(PARAM_EDIT_MODE, false);
        if (isEditMode) {
            int index = getIntent().getIntExtra(PARAM_TIME_CONFIG, 0);
            timeLockCondition = (TimeLockCondition)lockConfigManager.getLockCondition(index);
        }


        setTitle(R.string.select_time);
        initToolBar();
        initViews();
    }


    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_header);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    /**
     * 为TimePicker设置时间
     * @param timePicker 待设定时间的TimePicker
     * @param calendar 要设定的日期
     */
    private void setTimePickTime (TimePicker timePicker, Calendar calendar) {
        if (Build.VERSION.SDK_INT < 23) {
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        } else {
            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
        }
    }

    /**
     * 从TimePick获取时间
     * @param timePicker 获取时间的TimePicker
     * @return 获取的时间
     */
    private Calendar getTimePickTime (TimePicker timePicker) {
        Calendar calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT < 23) {
            calendar.set(0, 0, 0, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
        } else {
            calendar.set(0, 0, 0, timePicker.getHour(), timePicker.getMinute());
        }
        return calendar;
    }

    /**
     * 初始化各种View
     */
    private void initViews() {
        tp_start_time = (TimePicker) findViewById(R.id.tp_start_time);
        tp_end_time = (TimePicker) findViewById(R.id.tp_end_time);
        cb_day1 = (CheckBox) findViewById(R.id.cb_day1);
        cb_day2 = (CheckBox) findViewById(R.id.cb_day2);
        cb_day3 = (CheckBox) findViewById(R.id.cb_day3);
        cb_day4 = (CheckBox) findViewById(R.id.cb_day4);
        cb_day5 = (CheckBox) findViewById(R.id.cb_day5);
        cb_day6 = (CheckBox) findViewById(R.id.cb_day6);
        cb_day7 = (CheckBox) findViewById(R.id.cb_day7);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_ok = (Button) findViewById(R.id.bt_ok);

        // 如果是编辑参数，加载数据到界面上
        if (isEditMode) {
            // 解析并设置时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(SystemUtils.formatDate(timeLockCondition.getStartTime(), "HH:mm:ss"));
            setTimePickTime(tp_start_time, calendar);
            calendar.setTime(SystemUtils.formatDate(timeLockCondition.getEndTime(), "HH:mm:ss"));
            setTimePickTime(tp_end_time, calendar);

            // 解析并设置星期
            cb_day1.setChecked(timeLockCondition.isDaySet(TimeLockCondition.DAY1));
            cb_day2.setChecked(timeLockCondition.isDaySet(TimeLockCondition.DAY2));
            cb_day3.setChecked(timeLockCondition.isDaySet(TimeLockCondition.DAY3));
            cb_day4.setChecked(timeLockCondition.isDaySet(TimeLockCondition.DAY4));
            cb_day5.setChecked(timeLockCondition.isDaySet(TimeLockCondition.DAY5));
            cb_day6.setChecked(timeLockCondition.isDaySet(TimeLockCondition.DAY6));
            cb_day7.setChecked(timeLockCondition.isDaySet(TimeLockCondition.DAY7));
        }

        // 取消键的处理
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 确认键的处理
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 解析界面数据
                int day = 0;
                day |= cb_day1.isChecked() ? TimeLockCondition.DAY1 : 0;
                day |= cb_day2.isChecked() ? TimeLockCondition.DAY2 : 0;
                day |= cb_day3.isChecked() ? TimeLockCondition.DAY3 : 0;
                day |= cb_day4.isChecked() ? TimeLockCondition.DAY4 : 0;
                day |= cb_day5.isChecked() ? TimeLockCondition.DAY5 : 0;
                day |= cb_day6.isChecked() ? TimeLockCondition.DAY6 : 0;
                day |= cb_day7.isChecked() ? TimeLockCondition.DAY7 : 0;

                Calendar calendar = getTimePickTime(tp_start_time);
                String startTime = SystemUtils.formatDate(calendar.getTime(), "HH:mm:ss");
                calendar = getTimePickTime(tp_end_time);
                String endTime = SystemUtils.formatDate(calendar.getTime(), "HH:mm:ss");

                TimeLockCondition newConfig = new TimeLockCondition();
                newConfig.setDay(day);
                newConfig.setStartTime(startTime);
                newConfig.setEndTime(endTime);
                if (isEditMode) {
                    lockConfigManager.updateLockCondition(timeLockCondition, newConfig);
                } else {
                    lockConfigManager.addLockConditionIntoMode(newConfig);
                }

                // 结束编辑
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    /**
     * Activiy的onCreateOptionMenu回调
     *
     * @param menu 创建的菜单
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 处理返回按钮按下的响应
     *
     * @param item 被按下的项
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * 按返回键时的处理
     */
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }
}
