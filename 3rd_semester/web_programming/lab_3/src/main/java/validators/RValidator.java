package validators;

import jakarta.faces.validator.FacesValidator;

@FacesValidator("RValidator")
public class RValidator extends DoubleValidator {
    public RValidator() {
        super("R", 1, 5);
    }
}
