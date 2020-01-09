package at.fhtw.ode.helios.ui.encoders;

import com.vaadin.flow.templatemodel.ModelEncoder;

/**
 * @author Vaadin Ltd
 */

public class LongToStringEncoder implements ModelEncoder<Long, String> {

    @Override
    public String encode(Long modelValue) {
        if (modelValue == null)
            return null;
        return modelValue.toString();
    }

    @Override
    public Long decode(String presentationValue) {
        if (presentationValue == null)
            return null;
        return Long.parseLong(presentationValue);
    }

}
