package validators;

import jakarta.faces.convert.FacesConverter;

@FacesConverter("YConverter")
public class YConverter extends DoubleConverter {
    public YConverter() {
        super("Y");
    }
}
