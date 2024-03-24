package validators;

import jakarta.faces.convert.FacesConverter;

@FacesConverter("XConverter")
public class XConverter extends DoubleConverter {
    public XConverter() {
        super("X");
    }
}
