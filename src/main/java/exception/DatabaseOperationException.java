package exception;

public class DatabaseOperationException extends RuntimeException {

    /** */
    private static final long serialVersionUID = 1L;

    public DatabaseOperationException(String error) {
        super(error);
    }

}