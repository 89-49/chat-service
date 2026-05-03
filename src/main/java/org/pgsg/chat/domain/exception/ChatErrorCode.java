package org.pgsg.chat.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.pgsg.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {

    // --- 1. 채팅방 기본 상태 관련  ---
    CHAT_ROOM_NOT_FOUND("chat.room.not-found"), // 방을 찾을 수 없을 때
    CHAT_ROOM_ALREADY_EXISTS("chat.room.already-exists"), // 이미 방이 만들어져 있을 때
    CHAT_ROOM_INVALID_STATUS_TRANSITION("chat.room.invalid-status-transition"), // 상태 변경이 불가능할 때

    // --- 2. 사용자 검증  ---
    CHAT_VALIDATION_SELLER_ID_REQUIRED("chat.validation.seller-id.required"), // 판매자 번호가 없을 때
    CHAT_VALIDATION_SELLER_ID_INVALID("chat.validation.seller-id.invalid"), // 판매자 번호가 이상할 때
    CHAT_VALIDATION_BUYER_ID_REQUIRED("chat.validation.buyer-id.required"), // 구매자 번호가 없을 때
    CHAT_VALIDATION_SELLER_NICKNAME_REQUIRED("chat.validation.seller-nickname.required"), // 판매자 이름이 없을 때
    CHAT_VALIDATION_BUYER_NICKNAME_REQUIRED("chat.validation.buyer-nickname.required"), // 구매자 이름이 없을 때

    // --- 3. 거래 및 상품 검증  ---
    CHAT_VALIDATION_PRODUCT_ID_REQUIRED("chat.validation.product-id.required"), // 상품 번호가 없을 때
    CHAT_VALIDATION_PRODUCT_NAME_REQUIRED("chat.validation.product-name.required"), // 상품 이름이 없을 때
    CHAT_VALIDATION_TRADE_ID_REQUIRED("chat.validation.trade-id.required"), // 거래 번호가 없을 때
    CHAT_VALIDATION_TRADE_INFO_REQUIRED("chat.validation.trade-info.required"), // 취소/완료 정보 자체가 통째로 없을 때

    // --- 4. 채팅방 현재 상태 제한  ---
    CHAT_STATUS_ALREADY_COMPLETED("chat.status.already-completed"), // 이미 거래가 완료된 방일 때
    CHAT_STATUS_ALREADY_CANCELED("chat.status.already-cancelled"), // 이미 취소된 방일 때

    // --- 5. 메시지 및 발신자 오류  ---
    CHAT_MESSAGE_EMPTY("chat.message.empty"), // 아무 글자도 안 쓰고 보내기 눌렀을 때
    CHAT_SENDER_INVALID_TYPE("chat.sender.invalid-type"); // 보낸 사람이 판매자도 구매자도 아닐 때


    private final String errorKey;

}
