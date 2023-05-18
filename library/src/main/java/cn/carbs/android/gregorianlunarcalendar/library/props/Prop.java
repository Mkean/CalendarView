package cn.carbs.android.gregorianlunarcalendar.library.props;

public abstract class Prop<T> {

    private T value;

    public Prop() {
    }

    public Prop(T defaultValue) {
        value = defaultValue;
    }

    abstract T toValue(Object value);

    public void setValue(Object value) {
        this.value = toValue(value);
    }

    public T getValue() {
        return value;
    }

}
