package com.smile.date_picker.wheelFunctions;

import com.smile.date_picker.pickers.Picker;
import com.smile.date_picker.ui.WheelChangeListener;
import com.smile.date_picker.wheels.Wheel;

public class AddOnChangeListener implements WheelFunction {

    private final WheelChangeListener onChangeListener;

    public AddOnChangeListener(WheelChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    @Override
    public void apply(final Wheel wheel) {
        wheel.picker.setOnValueChangedListener(new Picker.OnValueChangeListener() {
            @Override
            public void onValueChange() {
                onChangeListener.onChange(wheel);
            }
        });
    }
}


