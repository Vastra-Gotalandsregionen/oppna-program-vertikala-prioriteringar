package se.vgregion.verticalprio.controllers;

import java.io.Serializable;

public class MessageHome implements Serializable {
    private String message = "";

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}