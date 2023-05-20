package com.smile.date_picker.wheelFunctions;

import com.smile.date_picker.wheels.Wheel;

import java.util.Calendar;

public class SetDate implements WheelFunction {

    private Calendar date;

    public SetDate(Calendar date) {
        this.date = date;
    }

    @Override
    public void apply(Wheel wheel) {
        wheel.setValue(date);
    }
}


