package Validate;

import Domain.Message;
import Exceptions.ValidateException;

import java.util.Objects;

/**
 * valideaza o relatie de prietenie dintre doi utilizatori
 */
public class MessageValidator implements Validation<Integer, Message> {
    /**
     * @param message element de tip mesaj care va fi validat
     * @throws ValidateException arunca eroare de validare daca id-urile expeditor/destinatar nu exista,
     * id-ul reply este negativ, data sau corpul sunt vide
     */
    @Override
    public void genericValidate(Message message) throws ValidateException {
        String err = "";

        if(message.getId() < 1)
            err += "Id invalid!\n";
        if(message.getFrom() < 1)
            err += "Utilizatorul care trimite invalid!\n";
        if(message.getTo() < 1)
            err += "Utilizatorul care primeste invalid!\n";
        if(message.getMessage() != null)
            if(Objects.equals(message.getMessage(), ""))
                err += "Mesaj invalid!\n";
        if(message.getDate() == null)
            err += "Data invalida!\n";
        if(message.getId_reply() != null)
            if(message.getId_reply() <= 0)
                err += "Id reply invalid!\n";

        if(err.length() > 0)
            throw new ValidateException(err);
    }
}
