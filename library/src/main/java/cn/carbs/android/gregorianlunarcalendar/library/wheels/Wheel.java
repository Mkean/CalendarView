package cn.carbs.android.gregorianlunarcalendar.library.wheels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.carbs.android.gregorianlunarcalendar.library.State;
import cn.carbs.android.gregorianlunarcalendar.library.pickers.Picker;

public abstract class Wheel {

    protected final State state;
    private Calendar userSetValue;

    abstract boolean visible();

    abstract String getFormatPattern();

    abstract ArrayList<String> getValues();

    public String toDisplayValue(String value) {
        return value;
    }

    private ArrayList<String> values = new ArrayList<>();
    public Picker picker;
    public SimpleDateFormat format;

    public Wheel(Picker picker, State state) {
        this.state = state;
        this.picker = picker;
        this.format = new SimpleDateFormat(getFormatPattern(), Locale.getDefault());
    }
}
