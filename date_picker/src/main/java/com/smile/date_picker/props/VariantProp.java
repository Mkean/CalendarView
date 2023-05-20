package com.smile.date_picker.props;

import com.facebook.react.bridge.Dynamic;
import com.smile.date_picker.models.Variant;

public class VariantProp extends Prop<Variant> {
    public static final String name = "androidVariant";

    @Override
    public Variant toValue(Dynamic value) {
        return Variant.valueOf(value.asString());
    }

}