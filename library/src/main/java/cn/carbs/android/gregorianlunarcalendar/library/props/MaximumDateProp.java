package cn.carbs.android.gregorianlunarcalendar.library.props;

public class MaximumDateProp  extends Prop<String>{

    public static final String name = "MaximumDate";

    @Override
    String toValue(Object value) {
        return value.toString();
    }
}
