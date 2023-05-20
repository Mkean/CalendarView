package com.smile.date_picker.props;

import com.facebook.react.bridge.Dynamic;

public class MinuteIntervalProp extends Prop<Integer> {
    public static final String name = "minuteInterval";

    public MinuteIntervalProp(){
        super(1);
    }

    public Integer toValue(Dynamic value){
        return value.asInt();
    }

}
