package validators;

import jakarta.faces.convert.Converter;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.ConverterException;

abstract class DoubleConverter implements Converter {
    private String VarName;

    public DoubleConverter(String varName) {
        this.VarName = varName;
    }
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String enteredValue) {
        try {
            return Double.parseDouble(enteredValue);
        } catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Parameter Error", VarName + " must be a number."));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object == null) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Parameter Error", VarName + " is required."));
        }
        return object.toString();
    }
}