package cn.carbs.android.gregorianlunarcalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import cn.carbs.android.gregorianlunarcalendar.library.NumberPickerView;
import cn.carbs.android.gregorianlunarcalendar.library.data.ChineseCalendar;
import cn.carbs.android.gregorianlunarcalendar.library.util.Util;
import cn.carbs.android.gregorianlunarcalendar.library.view.GregorianLunarCalendarView;
import cn.carbs.android.indicatorview.library.IndicatorView;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener,
        IndicatorView.OnIndicatorChangedListener {

    //indicator view used to indicate and switch gregorien/lunar mode
    private IndicatorView mIndicatorView;
    private GregorianLunarCalendarView mGLCView;
    private TextView mChangedDate;
    private Button mButtonGetData;
    private Button mButtonShowDialog;
    private DialogGLC mDialog;

    private Calendar selectCalendar = Calendar.getInstance();

    private NumberPickerView picker;

    private String[] months = {"《水调歌头》", "明月几时有", "把酒问青天", "不知天上宫阙", "今夕是何年",
            "我欲乘风归去", "又恐琼楼玉宇", "高处不胜寒", "起舞弄清影", "何似在人间", "转朱阁",
            "低绮户", "照无眠", "不应有恨", "何事长向别时圆", "人有悲欢离合", "月有阴晴圆缺", "此事古难全", "但愿人长久", "千里共婵娟"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGLCView = (GregorianLunarCalendarView) this.findViewById(R.id.calendar_view);
//        mGLCView.init();//init has no scroll effection, to today
//        mGLCView.setRange(Util.isoToCalendar("2023-04-18 00:00:00"), Calendar.getInstance());

        /*Calendar customizedCalendar = Calendar.getInstance();
        customizedCalendar.set((2012), 11, 12);//eg. 2012-12-12
        mGLCView.init(customizedCalendar);//to 2012-12-12*/

        picker = (NumberPickerView) findViewById(R.id.picker_month1);

        mIndicatorView = (IndicatorView) this.findViewById(R.id.indicator_view);
        mIndicatorView.setOnIndicatorChangedListener(this);

        mButtonGetData = (Button) this.findViewById(R.id.button_get_data);
        mButtonGetData.setOnClickListener(this);
        mButtonShowDialog = (Button) this.findViewById(R.id.button_in_dialog);
        mButtonShowDialog.setOnClickListener(this);
        mChangedDate = (TextView) this.findViewById(R.id.tv_changed_date);


        findViewById(R.id.button_start_time).setOnClickListener(this);
        findViewById(R.id.button_end_time).setOnClickListener(this);
        mGLCView.setOnDateChangedListener(new GregorianLunarCalendarView.OnDateChangedListener() {
            @Override
            public void onDateChanged(GregorianLunarCalendarView.CalendarData calendarData) {
                Calendar calendar = calendarData.getCalendar();
                selectCalendar = calendar;
                String showToast = "Gregorian : " + calendar.get(Calendar.YEAR) + "-"
                        + (calendar.get(Calendar.MONTH) + 1) + "-"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "\n"
                        + "Lunar     : " + calendar.get(ChineseCalendar.CHINESE_YEAR) + "-"
                        + (calendar.get(ChineseCalendar.CHINESE_MONTH)) + "-"
                        + calendar.get(ChineseCalendar.CHINESE_DATE);
                mChangedDate.setText(showToast);
            }
        });

        picker.setMinValue(2);
        picker.setDisplayedValues(months);
        picker.setMaxValue(5);
        picker.setValue(5);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_get_data:
                GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();
                Calendar calendar = calendarData.getCalendar();
                String showToast = "Gregorian : " + calendar.get(Calendar.YEAR) + "-"
                        + (calendar.get(Calendar.MONTH) + 1) + "-"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "\n"
                        + "Lunar     : " + calendar.get(ChineseCalendar.CHINESE_YEAR) + "-"
                        + (calendar.get(ChineseCalendar.CHINESE_MONTH)) + "-"
                        + calendar.get(ChineseCalendar.CHINESE_DATE);
                Toast.makeText(getApplicationContext(), showToast, Toast.LENGTH_LONG).show();
                break;
            case R.id.button_in_dialog:
                showInDialog();
                break;
            case R.id.button_start_time:
                mGLCView.setRange(selectCalendar, Calendar.getInstance());
                break;
            case R.id.button_end_time:
                Log.e("Main", "day: " + selectCalendar.get(Calendar.DATE));
                mGLCView.setRange(Util.isoToCalendar("2023-04-16 00:00:00"), selectCalendar);
                break;
            default:
                break;
        }
    }

    private void showInDialog() {
        if (mDialog == null) {
            mDialog = new DialogGLC(this);
        }
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        } else {
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
            //better initialize NumberPickerView's data (or set a certain value)
            // every time setting up reusable dialog
            mDialog.initCalendar();
        }
    }


    @Override
    public void onIndicatorChanged(int oldSelectedIndex, int newSelectedIndex) {
        if (newSelectedIndex == 0) {
            toGregorianMode();
        } else if (newSelectedIndex == 1) {
            toLunarMode();
        }
    }

    private void toGregorianMode() {
        mGLCView.toGregorianMode();
    }

    private void toLunarMode() {
        mGLCView.toLunarMode();
    }
}

