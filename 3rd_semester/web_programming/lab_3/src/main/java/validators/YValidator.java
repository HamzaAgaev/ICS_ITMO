package validators;

import jakarta.faces.validator.FacesValidator;

@FacesValidator("YValidator")
public class YValidator extends DoubleValidator {
    public YValidator() {
        super("Y", -3, 5);
    }
}