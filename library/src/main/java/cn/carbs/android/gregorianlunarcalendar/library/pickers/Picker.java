package cn.carbs.android.gregorianlunarcalendar.library.pickers;

import android.view.View;

public interface Picker {

    void setMaxValue(int value);

    void setMinValue(int value);

    int getMaxValue();

    int getMinValue();

    int getValue();

    void setValue(int value);

    View getView();

    void setVisibility(int visibility);


    interface OnValueChangeListener {
        void onValueChange();
    }
}
