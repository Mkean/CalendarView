package com.smile.date_picker.wheelFunctions;

import com.smile.date_picker.wheels.Wheel;

public class Refresh implements WheelFunction {

    @Override
    public void apply(Wheel wheel) {
        wheel.refresh();
    }
}


