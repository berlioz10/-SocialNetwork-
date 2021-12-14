package Domain;

/**
 * @param <Id> este tipul generic pentru id-ul unei instante apartinand acestei clase
 */
public interface Identifiable<Id> {

    /**
     * @return returneaza id-ul elementului
     */
    Id getId();

}
