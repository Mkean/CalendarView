package cn.carbs.android.gregorianlunarcalendar.library.props;

public class MinimumDateProp extends Prop<String> {

    public static final String name = "minimumDate";

    @Override
    String toValue(Object value) {
        return value.toString();
    }
}
