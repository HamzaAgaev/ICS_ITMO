package validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.regex.Pattern;

abstract class DoubleValidator implements Validator {
    private final String numPattern = "^-?[0-9]+(\\.[0-9]+)?$";
    private String VarName;
    private double minVarValue;
    private double maxVarValue;

    public DoubleValidator(String varName, double minVarValue, double maxVarValue) {
        this.VarName = varName;
        this.minVarValue = minVarValue;
        this.maxVarValue = maxVarValue;
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
        if (object == null) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Parameter Error",
                            VarName + " is required."));
        }
        String enteredX = object.toString();
        enteredX = enteredX.replace(',', '.');

        if (!Pattern.matches(numPattern, enteredX)) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Parameter Error",
                            "Enter Number for " + VarName + ", not a String."));
        }

        double X = Double.parseDouble(enteredX);

        if (X < minVarValue || X > maxVarValue) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Parameter Error",
                            VarName + " is out of range [" + minVarValue + "; " + maxVarValue + "]."));
        }
    }
}
