package cn.carbs.android.gregorianlunarcalendar.library.wheels;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import cn.carbs.android.gregorianlunarcalendar.library.State;
import cn.carbs.android.gregorianlunarcalendar.library.models.Mode;
import cn.carbs.android.gregorianlunarcalendar.library.pickers.Picker;

public class MonthWheel extends Wheel {

    private int defaultStartMonth;
    private int defaultEndMonth;

    public MonthWheel(Picker picker, State state) {
        super(picker, state);
        this.defaultStartMonth = 1;
        this.defaultEndMonth = 12;
    }

    @Override
    ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int startMonth = getStartMonth();
        int endMonth = getEndMonth();
        int span = endMonth - startMonth + 1;

        cal.set(Calendar.MONTH, startMonth);
        for (int i = 0; i < span; i++) {
            values.add(String.valueOf(startMonth + i));
            cal.add(Calendar.MONTH, 1);
        }

        return values;
    }

    private int getEndMonth() {
        if (state.getMaximumDate() == null) {
            return this.defaultEndMonth;
        }

        return state.getMaximumDate().get(Calendar.MONTH) + 1;
    }

    private int getStartMonth() {
        if (state.getMinimumDate() == null) {
            return this.defaultStartMonth;
        }
        return state.getMinimumDate().get(Calendar.MONTH) + 1;
    }


    @Override
    boolean visible() {
        return state.getMode() == Mode.DATE;
    }

    @Override
    String getFormatPattern() {
        return "MM";
    }


}
