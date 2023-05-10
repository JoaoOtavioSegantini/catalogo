package com.curso.admin.catalogo.domain.exceptions;

import com.curso.admin.catalogo.domain.validation.handler.Notification;

public class NotificationException extends DomainException {
    
    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
