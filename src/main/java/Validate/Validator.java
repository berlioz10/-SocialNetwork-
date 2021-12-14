package Validate;

import Domain.Identifiable;
import Exceptions.ValidateException;

/**
 * @param <Id> id-ul generic al unui element
 * @param <T>  instanta a clasei Identifiable
 *             valideaza un element generic folosind logica strategy pattern
 */
public class Validator<Id, T extends Identifiable<Id>> {
    private Validation<Id, T> validation;

    /**
     * @param validation validator generic conform strategy pattern
     */
    public Validator(Validation<Id, T> validation) {
        this.validation = validation;
    }

    /**
     * @param t elementul generic care va fi validat
     * @throws ValidateException arunca eroare specifica contextului daca elementul generic nu este valid
     */
    public void validate(T t) throws ValidateException {
        validation.genericValidate(t);
    }

}
