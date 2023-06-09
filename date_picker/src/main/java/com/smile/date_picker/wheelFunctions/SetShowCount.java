package com.smile.date_picker.wheelFunctions;

import com.smile.date_picker.wheels.Wheel;

public class SetShowCount implements WheelFunction {

    private final int count;

    public SetShowCount(int count) {
        this.count = count;
    }

    @Override
    public void apply(Wheel wheel) {
        wheel.picker.setShownCount(this.count);
    }
}


