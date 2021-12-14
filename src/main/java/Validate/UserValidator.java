package Validate;

import Domain.User;
import Exceptions.ValidateException;

import java.util.Objects;

/**
 * valideaza un utilizator
 */
public class UserValidator implements Validation<Integer, User> {
    private String message;

    /**
     * mesajul specific este intializat vid in constructor
     */
    public UserValidator() {
        message = "";
    }

    /**
     * @param user element de tip utilizator care va fi validat
     * @throws ValidateException arunca eroare de validare cu mesajul creeat pe parcurs daca exista
     *                           verifica daca elementul are id numar natural si nume/prenume siruri nevide
     */
    @Override
    public void genericValidate(User user) throws ValidateException {
        message = "";
        if (user.getId() < 1)
            message += "Id invalid!\n";
        if (Objects.equals(user.getFirstName(), ""))
            message += "Prenume invalid!\n";
        if (Objects.equals(user.getSurname(), ""))
            message += "Nume invalid!\n";
        if (message.length() > 0)
            throw new ValidateException(message);
    }
}
