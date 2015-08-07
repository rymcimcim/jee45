package pl.jeeweb.zadanie4.controllers;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class PeselValidator implements Validator {

    private byte PESEL[] = new byte[11];

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String pesel = (String) value;

        for (int i = 0; i < 11; i++) {
            PESEL[i] = Byte.parseByte(pesel.substring(i, i + 1));
        }

        int peselSum = PESEL[0] + (PESEL[1] * 3) + (PESEL[2] * 7) + (PESEL[3] * 9)
                + PESEL[4] + (PESEL[5] * 3) + (PESEL[6] * 7) + (PESEL[7] * 9) + PESEL[8]
                + (PESEL[9] * 3);
        int controlDigit = 10 - (peselSum % 10);
        if (controlDigit == 10) {
            controlDigit = 0;
        }
        if (controlDigit != PESEL[10]) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Musisz podać właściwy numer PESEL.", null));
        }
    }

}
