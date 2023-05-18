package cn.carbs.android.gregorianlunarcalendar.library;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import cn.carbs.android.gregorianlunarcalendar.library.models.Is24HourSource;
import cn.carbs.android.gregorianlunarcalendar.library.models.Mode;
import cn.carbs.android.gregorianlunarcalendar.library.props.*;
import cn.carbs.android.gregorianlunarcalendar.library.util.Util;

public class State {

    private Calendar lastSelectedDate = null;
    private final DateProp dateProp = new DateProp();
    private final ModeProp modeProp = new ModeProp();
    private final MinuteIntervalProp minuteIntervalProp = new MinuteIntervalProp();
    private final MinimumDateProp minimumDateProp = new MinimumDateProp();
    private final MaximumDateProp maximumDateProp = new MaximumDateProp();

    private final HashMap<String, Prop> props = new HashMap<String, Prop>() {{
        put(DateProp.name, dateProp);
        put(ModeProp.name, modeProp);
        put(MinuteIntervalProp.name, minuteIntervalProp);
        put(MinimumDateProp.name, minimumDateProp);
        put(MaximumDateProp.name, maximumDateProp);
    }};

    public DerivedData derived;

    public State() {
        derived = new DerivedData(this);
    }

    private Prop getProp(String name) {
        return (Prop) props.get(name);
    }

    void setProp(String propName, Object value) {
        getProp(propName).setValue(value);
    }

    public Mode getMode() {
        return (Mode) modeProp.getValue();
    }

    public int getMinuteInterval() {
        return (int) minuteIntervalProp.getValue();
    }

    public Calendar getMinimumDate() {
        return Util.isoToCalendar(minimumDateProp.getValue());
    }

    public Calendar getMaximumDate() {
        return Util.isoToCalendar(maximumDateProp.getValue());
    }

    public String getIsoDate() {
        return (String) dateProp.getValue();
    }

    private Calendar getDate() {
        return Util.isoToCalendar(getIsoDate());
    }

    // The date the picker is suppose to display.
    // Includes minute rounding to desired minute interval.
    public Calendar getPickerDate() {
        Calendar cal = getDate();
        int minuteInterval = getMinuteInterval();
        if (minuteInterval <= 1) {
            return cal;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        int exactMinute = Integer.parseInt(minuteFormat.format(cal.getTime()));
        int minutesSinceLastInterval = exactMinute % minuteInterval;
        cal.add(Calendar.MINUTE, -minutesSinceLastInterval);
        return (Calendar) cal.clone();
    }

    public Calendar getLastSelectedDate() {
        return lastSelectedDate;
    }

    public void setLastSelectedDate(Calendar date) {
        lastSelectedDate = date;
    }
}
