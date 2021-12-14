package Exceptions;


public class BusinessException extends Exception {
    /**
     * @param message mesaj specific contextului generat de raportarea id-ului fiecarui element la service
     */
    public BusinessException(String message) {
        super(message);
    }
}

