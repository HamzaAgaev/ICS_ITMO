package validators;

import jakarta.faces.validator.FacesValidator;

@FacesValidator("XValidator")
public class XValidator extends DoubleValidator {
    public XValidator() {
        super("X", -3, 3);
    }
}