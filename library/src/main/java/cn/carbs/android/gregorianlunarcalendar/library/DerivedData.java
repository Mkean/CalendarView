package cn.carbs.android.gregorianlunarcalendar.library;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;

import cn.carbs.android.gregorianlunarcalendar.library.models.Mode;
import cn.carbs.android.gregorianlunarcalendar.library.models.WheelType;
import cn.carbs.android.gregorianlunarcalendar.library.util.Util;

public class DerivedData {

    private final State state;

    DerivedData(State state) {
        this.state = state;
    }

    public ArrayList<WheelType> getVisibleWheels() {
        ArrayList<WheelType> visibleWheels = new ArrayList<>();
        Mode mode = state.getMode();
        switch (mode) {
            case DATETIME:
                visibleWheels.add(WheelType.DAY);
                visibleWheels.add(WheelType.HOUR);
                visibleWheels.add(WheelType.MINUTE);
                break;
            case TIME:
                visibleWheels.add(WheelType.HOUR);
                visibleWheels.add(WheelType.MINUTE);
                break;
            case DATE:
                visibleWheels.add(WheelType.YEAR);
                visibleWheels.add(WheelType.MONTH);
                visibleWheels.add(WheelType.DATE);
                break;
            default:
                break;
        }
        return visibleWheels;
    }

    public String getLastDate(){
        Calendar lastSelectedDate = state.getLastSelectedDate();
        String initialDate = state.getIsoDate();
        if(lastSelectedDate != null) {
            return Util.dateToIso(lastSelectedDate);
        }
        return initialDate;
    }
}
