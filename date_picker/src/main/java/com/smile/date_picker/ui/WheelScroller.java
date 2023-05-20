package com.smile.date_picker.ui;

import com.smile.date_picker.pickers.Picker;
import com.smile.date_picker.wheels.Wheel;

public class WheelScroller {

    public void scroll(Wheel wheel, int scrollTimes) {
        Picker picker = wheel.picker;
        int currentIndex = picker.getValue();
        int maxValue = picker.getMaxValue();
        boolean isWrapping = picker.getWrapSelectorWheel();
        int nextValue = currentIndex + scrollTimes;
        if (nextValue <= maxValue || isWrapping) {
            picker.smoothScrollToValue(nextValue % (maxValue + 1));
        }
    }
}
