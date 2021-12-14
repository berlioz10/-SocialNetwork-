package Exceptions;

public class RepoException extends Exception {
    /**
     * @param message mesaj specific contextului generat de raportarea id-ului fiecarui element la repozitoriu
     */
    public RepoException(String message) {
        super(message);
    }
}
