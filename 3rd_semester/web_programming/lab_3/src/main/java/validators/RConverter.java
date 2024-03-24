package validators;

import jakarta.faces.convert.FacesConverter;

@FacesConverter("RConverter")
public class RConverter extends DoubleConverter {
    public RConverter() {
        super("R");
    }
}
