package org.pgsg.chat.domain.model;

import org.pgsg.chat.domain.exception.ChatErrorCode;
import org.pgsg.chat.domain.exception.ChatServiceException;

public enum SenderType {
    SELLER,
    BUYER;

    public static SenderType from(String value){
        try {
            return SenderType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ChatServiceException(ChatErrorCode.CHAT_SENDER_INVALID_TYPE);
        }
    }
}
