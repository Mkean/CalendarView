package cn.carbs.android.gregorianlunarcalendar.library.props;

public class MinuteIntervalProp extends Prop<Integer> {

    public static final String name = "minuteInterval";

    @Override
    Integer toValue(Object value) {
        return Integer.parseInt(value.toString());
    }
}
