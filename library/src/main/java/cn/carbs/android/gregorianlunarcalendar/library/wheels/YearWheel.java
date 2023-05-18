package cn.carbs.android.gregorianlunarcalendar.library.wheels;

import java.util.ArrayList;
import java.util.Calendar;

import cn.carbs.android.gregorianlunarcalendar.library.State;
import cn.carbs.android.gregorianlunarcalendar.library.models.Mode;
import cn.carbs.android.gregorianlunarcalendar.library.pickers.Picker;

public class YearWheel extends Wheel {

    private int defaultStartYear;
    private int defaultEndYear;

    public YearWheel(final Picker picker, final State state) {
        super(picker, state);
        this.defaultStartYear = 1900;
        this.defaultEndYear = 2100;
    }

    @Override
    ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        final int startYear = getStartYear();
        final int endYear = getEndYear();
        int span = endYear - startYear + 1;

        cal.set(Calendar.YEAR, startYear);

        for (int i = 0; i < span; i++) {
            values.add(String.valueOf(startYear + i));
            cal.add(Calendar.YEAR, 1);
        }

        return values;
    }

    private int getEndYear() {
        if (state.getMaximumDate() == null) {
            return this.defaultEndYear;
        }
        return state.getMaximumDate().get(Calendar.YEAR);
    }

    private int getStartYear() {
        if (state.getMinimumDate() == null) {
            return this.defaultStartYear;
        }
        return state.getMinimumDate().get(Calendar.YEAR);
    }

    @Override
    boolean visible() {
        return state.getMode() == Mode.DATE;
    }

    @Override
    String getFormatPattern() {
        return "yyyy";
    }
}
