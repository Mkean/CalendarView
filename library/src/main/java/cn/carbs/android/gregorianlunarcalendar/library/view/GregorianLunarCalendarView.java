package cn.carbs.android.gregorianlunarcalendar.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Calendar;

import cn.carbs.android.gregorianlunarcalendar.library.NumberPickerView;
import cn.carbs.android.gregorianlunarcalendar.library.R;
import cn.carbs.android.gregorianlunarcalendar.library.data.ChineseCalendar;
import cn.carbs.android.gregorianlunarcalendar.library.util.Util;

public class GregorianLunarCalendarView extends LinearLayout implements NumberPickerView.OnValueChangeListener {

    private static final int DEFAULT_GREGORIAN_COLOR = 0xff3388ff;
    private static final int DEFAULT_LUNAR_COLOR = 0xffee5544;
    private static final int DEFAULT_NORMAL_TEXT_COLOR = 0xFF555555;

    private int YEAR_START = 2023;
    private int YEAR_STOP = 2023;
    private int YEAR_SPAN = YEAR_STOP - YEAR_START + 1;

    private static final int MONTH_START = 1;

    private int MIN_MONTH_GREGORIAN = 1;
    private int MONTH_START_GREGORIAN = 1;
    private int MONTH_STOP_GREGORIAN = 12;
    private int MONTH_SPAN_GREGORIAN = MONTH_STOP_GREGORIAN - MONTH_START_GREGORIAN + 1;

    private static final int DAY_START = 1;
    private static final int DAY_STOP = 30;

    private int MIN_DAY_GREGORIAN = 1;
    private int DAY_START_GREGORIAN = 1;
    private int DAY_STOP_GREGORIAN = 31;
    private int DAY_SPAN_GREGORIAN = DAY_STOP_GREGORIAN - DAY_START_GREGORIAN + 1;

    private NumberPickerView mYearPickerView;
    private NumberPickerView mMonthPickerView;
    private NumberPickerView mDayPickerView;

    private int mThemeColorG = DEFAULT_GREGORIAN_COLOR;
    private int mThemeColorL = DEFAULT_LUNAR_COLOR;
    private int mNormalTextColor = DEFAULT_NORMAL_TEXT_COLOR;

    /**
     * display values
     */
    private String[] mDisplayYearsGregorian;
    private String[] mDisplayMonthsGregorian;
    private String[] mDisplayDaysGregorian;

    /**
     * true to use scroll anim when switch picker passively
     */
    private boolean mScrollAnim = true;

    private OnDateChangedListener mOnDateChangedListener;

    public GregorianLunarCalendarView(Context context) {
        super(context);
        initInternal(context);
    }

    public GregorianLunarCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initInternal(context);
    }

    public GregorianLunarCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttr(context, attrs);
        initInternal(context);
    }

    private void initInternal(Context context) {
        View contentView = inflate(context, R.layout.view_gregorian_lunar_calendar, this);

        mYearPickerView = (NumberPickerView) contentView.findViewById(R.id.picker_year);
        mMonthPickerView = (NumberPickerView) contentView.findViewById(R.id.picker_month);
        mDayPickerView = (NumberPickerView) contentView.findViewById(R.id.picker_day);

        mYearPickerView.setOnValueChangedListener(this);
        mMonthPickerView.setOnValueChangedListener(this);
        mDayPickerView.setOnValueChangedListener(this);
        setRange(Util.isoToCalendar("2021-03-18 00:00:00"), Calendar.getInstance());
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GregorianLunarCalendarView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.GregorianLunarCalendarView_glcv_ScrollAnimation) {
                mScrollAnim = a.getBoolean(attr, true);
            } else if (attr == R.styleable.GregorianLunarCalendarView_glcv_GregorianThemeColor) {
                mThemeColorG = a.getColor(attr, DEFAULT_GREGORIAN_COLOR);
            }
            if (attr == R.styleable.GregorianLunarCalendarView_glcv_LunarThemeColor) {
                mThemeColorL = a.getColor(attr, DEFAULT_LUNAR_COLOR);
            }
            if (attr == R.styleable.GregorianLunarCalendarView_glcv_NormalTextColor) {
                mNormalTextColor = a.getColor(attr, DEFAULT_NORMAL_TEXT_COLOR);
            }
        }
        a.recycle();
    }

    public void init() {
        init(Calendar.getInstance());
    }

    public void init(Calendar calendar) {
        setColor(mThemeColorG, mNormalTextColor);
        setConfigs(calendar, false);
    }

    private static final String TAG = "CalendarView";

    public void setRange(Calendar minCalendar, Calendar maxCalendar) {

        int startYear = minCalendar.get(Calendar.YEAR);
        int startMonth = minCalendar.get(Calendar.MONTH) + 1;
        int startDate = minCalendar.get(Calendar.DATE);


        int endYear = maxCalendar.get(Calendar.YEAR);
        int endMonth = maxCalendar.get(Calendar.MONTH) + 1;
        int endDate = maxCalendar.get(Calendar.DATE);


        YEAR_START = startYear;
        YEAR_STOP = endYear;
        YEAR_SPAN = endYear - startYear + 1;

        if (YEAR_SPAN == 1) {
            MONTH_START_GREGORIAN = startMonth;
        } else {
            MONTH_START_GREGORIAN = 1;
        }
        MIN_MONTH_GREGORIAN = startMonth;
        MONTH_STOP_GREGORIAN = endMonth;
        MONTH_SPAN_GREGORIAN = MONTH_STOP_GREGORIAN - MONTH_START_GREGORIAN + 1;

        if (YEAR_SPAN == 1 && MONTH_SPAN_GREGORIAN == 1) {
            DAY_START_GREGORIAN = startDate;
            DAY_SPAN_GREGORIAN = endDate - startDate + 1;
//            mDayPickerView.setWrapSelectorWheel(DAY_SPAN_GREGORIAN != 1);
        } else {
            DAY_START_GREGORIAN = 1;

            DAY_SPAN_GREGORIAN = endDate;
        }
        DAY_STOP_GREGORIAN = endDate;

        MIN_DAY_GREGORIAN = startDate;

        init(maxCalendar);
    }


    private void setConfigs(Calendar c, boolean anim) {
        if (c == null) {
            c = Calendar.getInstance();
        }
        if (!checkCalendarAvailable(c, YEAR_START, YEAR_STOP)) {
            c = adjustCalendarByLimit(c, YEAR_START, YEAR_STOP);
        }
        ChineseCalendar cc;
        if (c instanceof ChineseCalendar) {
            cc = (ChineseCalendar) c;
        } else {
            cc = new ChineseCalendar(c);
        }
        setDisplayValuesForAll(cc, anim);
    }

    private Calendar adjustCalendarByLimit(Calendar c, int yearStart, int yearStop) {
        int yearGrego = c.get(Calendar.YEAR);
        if (yearGrego < yearStart) {
            c.set(Calendar.YEAR, yearStart);
            c.set(Calendar.MONTH, MONTH_START_GREGORIAN);
            c.set(Calendar.DAY_OF_MONTH, DAY_START_GREGORIAN);
        }
        if (yearGrego > yearStop) {
            c.set(Calendar.YEAR, yearStop);
            c.set(Calendar.MONTH, MONTH_STOP_GREGORIAN - 1);
            int daySway = Util.getSumOfDayInMonthForGregorianByMonth(yearStop, MONTH_STOP_GREGORIAN);
            c.set(Calendar.DAY_OF_MONTH, daySway);
        }

        return c;
    }

    public void toGregorianMode() {
        setThemeColor(mThemeColorG);
//        setGregorian(true);
    }

    public void toLunarMode() {
        setThemeColor(mThemeColorL);
//        setGregorian(true);
    }

    public void setColor(int themeColor, int normalColor) {
        setThemeColor(themeColor);
        setNormalColor(normalColor);
    }

    public void setThemeColor(int themeColor) {
        mYearPickerView.setSelectedTextColor(themeColor);
        mYearPickerView.setHintTextColor(themeColor);
        mYearPickerView.setDividerColor(themeColor);
        mMonthPickerView.setSelectedTextColor(themeColor);
        mMonthPickerView.setHintTextColor(themeColor);
        mMonthPickerView.setDividerColor(themeColor);
        mDayPickerView.setSelectedTextColor(themeColor);
        mDayPickerView.setHintTextColor(themeColor);
        mDayPickerView.setDividerColor(themeColor);
    }

    public void setNormalColor(int normalColor) {
        mYearPickerView.setNormalTextColor(normalColor);
        mMonthPickerView.setNormalTextColor(normalColor);
        mDayPickerView.setNormalTextColor(normalColor);
    }

    private void setDisplayValuesForAll(ChineseCalendar cc, boolean anim) {
        setDisplayData();
        initValuesForY(cc, anim);
        initValuesForM(cc, anim);
        initValuesForD(cc, anim);
    }


    private void setDisplayData() {

        mDisplayYearsGregorian = new String[YEAR_SPAN];
        for (int i = 0; i < YEAR_SPAN; i++) {
            mDisplayYearsGregorian[i] = String.valueOf(YEAR_START + i);
        }

        mDisplayMonthsGregorian = new String[MONTH_SPAN_GREGORIAN];
        for (int i = 0; i < MONTH_SPAN_GREGORIAN; i++) {
            mDisplayMonthsGregorian[i] = String.valueOf(MONTH_START_GREGORIAN + i);
        }

        mDisplayDaysGregorian = new String[DAY_SPAN_GREGORIAN];
        for (int i = 0; i < DAY_SPAN_GREGORIAN; i++) {
            mDisplayDaysGregorian[i] = String.valueOf(DAY_START_GREGORIAN + i);
        }
    }

    // without scroll animation when init
    private void initValuesForY(ChineseCalendar cc, boolean anim) {
        int yearSway = cc.get(Calendar.YEAR);
        setValuesForPickerView(mYearPickerView, yearSway, YEAR_START, YEAR_STOP, mDisplayYearsGregorian, false, anim);

    }

    @SuppressLint("WrongConstant")
    private void initValuesForM(ChineseCalendar cc, boolean anim) {
        int monthStart;
        int monthStop;
        int monthSway;
        String[] newDisplayedVales = null;
        monthStart = MONTH_START_GREGORIAN;
        monthStop = MONTH_STOP_GREGORIAN;
        monthSway = cc.get(Calendar.MONTH) + 1;
        newDisplayedVales = mDisplayMonthsGregorian;


        setValuesForPickerView(mMonthPickerView, monthSway, monthStart, monthStop, newDisplayedVales, false, anim);
    }

    @SuppressLint("WrongConstant")
    private void initValuesForD(ChineseCalendar cc, boolean anim) {
        int dayStart = DAY_START_GREGORIAN;
        int dayStop = DAY_STOP_GREGORIAN;
        int daySway = cc.get(Calendar.DAY_OF_MONTH);
        mDayPickerView.setHintText(getContext().getResources().getString(R.string.day));

        setValuesForPickerView(mDayPickerView, daySway, dayStart, dayStop, mDisplayDaysGregorian, false, anim);

    }

    private void setValuesForPickerView(NumberPickerView pickerView, int newSway, int newStart, int newStop,
                                        String[] newDisplayedVales, boolean needRespond, boolean anim) {

        if (newDisplayedVales == null) {
            throw new IllegalArgumentException("newDisplayedVales should not be null.");
        } else if (newDisplayedVales.length == 0) {
            throw new IllegalArgumentException("newDisplayedVales's length should not be 0.");
        }
        int newSpan = newStop - newStart + 1;

        if (newDisplayedVales.length < newSpan) {
            throw new IllegalArgumentException("newDisplayedVales's length should not be less than newSpan.");
        }

        int oldStart = pickerView.getMinValue();
        int oldStop = pickerView.getMaxValue();
        int oldSpan = oldStop - oldStart + 1;
        int fromValue = pickerView.getValue();
        pickerView.setMinValue(newStart);
//        pickerView.setMinAndMaxShowIndex(newStart,newStop);
        if (newSpan > oldSpan) {
            pickerView.setDisplayedValues(newDisplayedVales);
            pickerView.setMaxValue(newStop);
        } else {
            pickerView.setMaxValue(newStop);
            pickerView.setDisplayedValues(newDisplayedVales);
        }

        if (mScrollAnim && anim) {
            int toValue = newSway;
            if (fromValue < newStart) {
                fromValue = newStart;
            }
            pickerView.smoothScrollToValue(fromValue, toValue, needRespond);
        } else {
            pickerView.setValue(newSway);
        }
    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        if (picker == null) {
            return;
        }
        setDisplayData();
        if (picker == mYearPickerView) {
            passiveUpdateMonthAndDay(oldVal, newVal);
        } else if (picker == mMonthPickerView) {
            int fixYear = mYearPickerView.getValue();
            passiveUpdateDay(fixYear, fixYear, oldVal, newVal);
        } else if (picker == mDayPickerView) {
            if (mOnDateChangedListener != null) {
                mOnDateChangedListener.onDateChanged(getCalendarData());
            }
        }
    }

    private void passiveUpdateMonthAndDay(int oldYearFix, int newYearFix) {
        int oldMonthSway = mMonthPickerView.getValue();

        if (newYearFix == YEAR_START) {
            mDisplayMonthsGregorian = new String[12 - MIN_MONTH_GREGORIAN + 1];
            for (int i = 0; i < 12 - MIN_MONTH_GREGORIAN + 1; i++) {
                mDisplayMonthsGregorian[i] = String.valueOf(MIN_MONTH_GREGORIAN + i);
            }


            int newMonthDay = Math.min(Math.max(oldMonthSway, MIN_MONTH_GREGORIAN), 12);
            setValuesForPickerView(mMonthPickerView, newMonthDay, MIN_MONTH_GREGORIAN, 12, mDisplayMonthsGregorian, true, true);
        } else if (newYearFix == YEAR_STOP) {
            int newMonthDay = Math.min(oldMonthSway, MONTH_STOP_GREGORIAN);
            setValuesForPickerView(mMonthPickerView, newMonthDay, MONTH_START, MONTH_SPAN_GREGORIAN, mDisplayMonthsGregorian, true, true);
        } else {
            int newMonthDay = Math.min(oldMonthSway, 12);
            mDisplayMonthsGregorian = new String[12];
            for (int i = 0; i < 12; i++) {
                mDisplayMonthsGregorian[i] = String.valueOf(MONTH_START + i);
            }
            setValuesForPickerView(mMonthPickerView, newMonthDay, MONTH_START, 12, mDisplayMonthsGregorian, true, true);
        }

        passiveUpdateDay(newYearFix, newYearFix, oldMonthSway, oldMonthSway);
    }

    private void passiveUpdateDay(int oldYear, int newYear, int oldMonth, int newMonth) {
        int oldDaySway = mDayPickerView.getValue();

        int newDayStop = Util.getSumOfDayInMonth(newYear, newMonth, true);

        if (newYear == YEAR_START && newMonth == MIN_MONTH_GREGORIAN) {

            mDisplayDaysGregorian = new String[newDayStop - MIN_DAY_GREGORIAN + 1];

            for (int i = 0; i < newDayStop - MIN_DAY_GREGORIAN + 1; i++) {
                mDisplayDaysGregorian[i] = String.valueOf(MIN_DAY_GREGORIAN + i);
            }

            int newDaySway = Math.min(Math.max(oldDaySway,MIN_DAY_GREGORIAN), newDayStop);
            setValuesForPickerView(mDayPickerView, newDaySway, MIN_DAY_GREGORIAN, newDayStop, mDisplayDaysGregorian, true, true);
            if (mOnDateChangedListener != null) {
                mOnDateChangedListener.onDateChanged(getCalendarData(newYear, newMonth, newDaySway));
            }
            return;
        }

        if (newYear == YEAR_STOP && newMonth == MONTH_STOP_GREGORIAN) {

            mDisplayDaysGregorian = new String[newDayStop];
            for (int i = 0; i < newDayStop; i++) {
                mDisplayDaysGregorian[i] = String.valueOf(DAY_START + i);
            }

            int newDaySway = Math.min(Math.min(oldDaySway, DAY_SPAN_GREGORIAN), newDayStop);
            setValuesForPickerView(mDayPickerView, newDaySway, DAY_START, DAY_SPAN_GREGORIAN, mDisplayDaysGregorian, true, true);
            if (mOnDateChangedListener != null) {
                mOnDateChangedListener.onDateChanged(getCalendarData(newYear, newMonth, newDaySway));
            }
            return;
        }
        mDisplayDaysGregorian = new String[31];
        for (int i = 0; i < 31; i++) {
            mDisplayDaysGregorian[i] = String.valueOf(1 + i);
        }

        int newDaySway = Math.min(oldDaySway, newDayStop);
        setValuesForPickerView(mDayPickerView, newDaySway, DAY_START, newDayStop, mDisplayDaysGregorian, true, true);
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(getCalendarData(newYear, newMonth, newDaySway));
        }
    }

    public void setGregorian(boolean anim) {

        ChineseCalendar cc = (ChineseCalendar) getCalendarData().getCalendar();//根据mIsGregorian收集数据
        if (!checkCalendarAvailable(cc, YEAR_START, YEAR_STOP)) {
            cc = (ChineseCalendar) adjustCalendarByLimit(cc, YEAR_START, YEAR_STOP);//调整改变后的界面的显示
        }
        setConfigs(cc, anim);//重新更新界面数据
    }

    private boolean checkCalendarAvailable(Calendar cc, int yearStart, int yearStop) {
        @SuppressLint("WrongConstant")
        int year = cc.get(Calendar.YEAR);
        return (yearStart <= year) && (year <= yearStop);
    }

    public View getNumberPickerYear() {
        return mYearPickerView;
    }

    public View getNumberPickerMonth() {
        return mMonthPickerView;
    }

    public View getNumberPickerDay() {
        return mDayPickerView;
    }

    public void setNumberPickerYearVisibility(int visibility) {
        setNumberPickerVisibility(mYearPickerView, visibility);
    }

    public void setNumberPickerMonthVisibility(int visibility) {
        setNumberPickerVisibility(mMonthPickerView, visibility);
    }

    public void setNumberPickerDayVisibility(int visibility) {
        setNumberPickerVisibility(mDayPickerView, visibility);
    }

    public void setNumberPickerVisibility(NumberPickerView view, int visibility) {
        if (view.getVisibility() == visibility) {
            return;
        } else if (visibility == View.GONE || visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            view.setVisibility(visibility);
        }
    }

    private CalendarData getCalendarData(int pickedYear, int pickedMonthSway, int pickedDay) {
        return new CalendarData(pickedYear, pickedMonthSway, pickedDay, true);
    }

    public CalendarData getCalendarData() {
        int pickedYear = mYearPickerView.getValue();
        int pickedMonthSway = mMonthPickerView.getValue();
        int pickedDay = mDayPickerView.getValue();
        return new CalendarData(pickedYear, pickedMonthSway, pickedDay, true);
    }

    public static class CalendarData {
        public boolean isGregorian = false;
        public int pickedYear;
        public int pickedMonthSway;
        public int pickedDay;

        /**
         * 获取数据示例与说明：
         * Gregorian : //公历
         * chineseCalendar.get(Calendar.YEAR)              //获取公历年份，范围[1900 ~ 2100]
         * chineseCalendar.get(Calendar.MONTH) + 1         //获取公历月份，范围[1 ~ 12]
         * chineseCalendar.get(Calendar.DAY_OF_MONTH)      //返回公历日，范围[1 ~ 30]
         * <p>
         * Lunar
         * chineseCalendar.get(ChineseCalendar.CHINESE_YEAR)   //返回农历年份，范围[1900 ~ 2100]
         * chineseCalendar.get(ChineseCalendar.CHINESE_MONTH)) //返回农历月份，范围[(-12) ~ (-1)] || [1 ~ 12]
         * //当有月份为闰月时，返回对应负值
         * //当月份非闰月时，返回对应的月份值
         * calendar.get(ChineseCalendar.CHINESE_DATE)         //返回农历日，范围[1 ~ 30]
         */
        public ChineseCalendar chineseCalendar;

        /**
         * model类的构造方法
         *
         * @param pickedYear      年
         * @param pickedMonthSway 月，公历农历均从1开始。农历如果有闰年，按照实际的顺序添加
         * @param pickedDay       日，从1开始，日期在月份中的显示数值
         * @param isGregorian     是否为公历
         */
        public CalendarData(int pickedYear, int pickedMonthSway, int pickedDay, boolean isGregorian) {
            this.pickedYear = pickedYear;
            this.pickedMonthSway = pickedMonthSway;
            this.pickedDay = pickedDay;
            this.isGregorian = isGregorian;
            initChineseCalendar();
        }

        /**
         * 初始化成员变量chineseCalendar，用来记录当前选中的时间。此成员变量同时存储了农历和公历的信息
         */
        private void initChineseCalendar() {
            if (isGregorian) {
                chineseCalendar = new ChineseCalendar(pickedYear, pickedMonthSway - 1, pickedDay);//公历日期构造方法
            } else {
                int y = pickedYear;
                int m = Util.convertMonthSwayToMonthLunarByYear(pickedMonthSway, pickedYear);
                int d = pickedDay;

                chineseCalendar = new ChineseCalendar(true, y, m, d);
            }
        }

        public Calendar getCalendar() {
            return chineseCalendar;
        }
    }

    public interface OnDateChangedListener {
        void onDateChanged(CalendarData calendarData);
    }

    public void setOnDateChangedListener(OnDateChangedListener listener) {
        mOnDateChangedListener = listener;
    }

}