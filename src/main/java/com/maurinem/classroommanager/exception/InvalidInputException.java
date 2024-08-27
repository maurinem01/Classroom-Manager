package com.maurinem.classroommanager.exception;

public class InvalidInputException extends RuntimeException {

    /** */
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String error) {
        super(error);
    }

}
