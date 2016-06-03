package com.trueweather.parsers;

/**
 * Created by dimaMJ on 21.05.2016.
 */
public class NotFoundError extends RuntimeException {

    public NotFoundError(String message) {
        super(message);
    }
}
