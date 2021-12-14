package Domain;

import java.sql.Timestamp;
import java.util.Objects;

/**
 *  clasa care retine date despre un mesaj: expeditor, destinatar, data, corp, este/ nu este reply
 */
public class Message implements Identifiable<Integer> {
    private final int id;
    private final int from;
    private final int to;
    private final String message;
    private final Timestamp date;
    private final Integer id_reply;

    /**
     * @param from este id-ul expeditorului
     * @param to este id-ul destinatarului
     * @param message este corpul mesajului
     * @param date este data la care a fost trimis
     * @param id_reply se aplica daca mesajul este un reply
     */
    public Message(int from, int to, String message, Timestamp date, Integer id_reply) {
        this.id = 1;
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.id_reply = id_reply;
    }

    /**
     * @param id este id-ul mesajului care va fi adaugat
     * @param from este id-ul expeditorului
     * @param to este id-ul destinatarului
     * @param message este corpul mesajului
     * @param date este data la care a fost trimis
     * @param id_reply se aplica daca mesajul este un reply
     */
    public Message(int id, int from, int to, String message, Timestamp date, Integer id_reply) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.id_reply = id_reply;
    }

    /**
     * @return expeditorul mesajului
     */
    public int getFrom() {
        return from;
    }

    /**
     * @return destinatarul mesajului
     */
    public int getTo() {
        return to;
    }

    /**
     * @return corpul mesajului
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return data la care a fost trimis mesajul
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * @return id-ul de reply daca este un reply
     */
    public Integer getId_reply() {
        return id_reply;
    }

    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @param o este obiectul pe care il comparam
     * @return returneaza true daca cele doua mesaje sunt egale sau false in caz contrar
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return id == message1.id && from == message1.from && to == message1.to && message.equals(message1.message) && date.equals(message1.date) && Objects.equals(id_reply, message1.id_reply);
    }

    /**
     * @return un numar intreg unic pentru fiecare mesaj diferit
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, message, date, id_reply);
    }

    /**
     * @return mesajul ca si sir de caractere ce contine corpul, expeditorul, destinatarul, data
     */
    @Override
    public String toString() {
        return "'" +
                message + "' de la " + from + " la " + to + " la data de " + date.toString();
    }
}
