package cn.carbs.android.gregorianlunarcalendar.library.props;

import cn.carbs.android.gregorianlunarcalendar.library.models.Mode;

public class ModeProp extends Prop<Mode> {

    public static final String name = "mode";

    @Override
    Mode toValue(Object value) {
        return Mode.valueOf(value.toString());
    }
}
