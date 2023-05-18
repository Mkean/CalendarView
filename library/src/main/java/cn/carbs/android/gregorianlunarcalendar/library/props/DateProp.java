package cn.carbs.android.gregorianlunarcalendar.library.props;

public class DateProp extends Prop<String> {

    public static final String name = "date";
    @Override
    String toValue(Object value) {
        return value.toString();
    }
}
