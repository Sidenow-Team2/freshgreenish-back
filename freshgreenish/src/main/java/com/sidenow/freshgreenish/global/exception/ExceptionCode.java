package com.sidenow.freshgreenish.global.exception;

import lombok.Getter;

public enum ExceptionCode {
    LIKES_NOT_FOUND(415, "존재하지 않는 좋아요입니다."),
    PRODUCT_NOT_FOUND(415, "존재하지 않는 상품 정보입니다."),
    ANSWER_NOT_FOUND(415, "존재하지 않는 답변 정보입니다."),
    QUESTION_NOT_FOUND(415, "존재하지 않는 질문 정보입니다"),
    USER_NOT_FOUND(415, "존재하지 않는 회원입니다."),
    REVIEW_NOT_FOUND(415, "존재하지 않는 후기입니다."),

    UNAUTHORIZED_FOR_UPDATE(403, "수정 권한이 없습니다."),
    UNAUTHORIZED_FOR_DELETE(403, "삭제 권한이 없습니다."),
    INVALID_ACCESS(403, "유효하지 않은 접근입니다."),
    INVALID_MEMBER(403, "회원 정보를 다시 확인하세요."),
    INVALID_OAUTH2(403, "지원하지 않는 OAuth2 프로바이더입니다."),
    INVALID_AUTH_TOKEN(403, "유효하지 않은 토큰입니다."),

    NICKNAME_ALREADY_EXIST(504, "이미 존재하는 닉네임입니다."),
    RESOURCE_ALREADY_EXIST(504, "이미 존재하는 데이터입니다."),

    UPLOAD_FAILED(504, "업로드가 실패했습니다."),
    PAYMENT_URL_REQUEST_FAILED(504, "결제 URL 요청을 실패했습니다."),

    NO_IMAGE(504, "이미지가 비어있습니다.");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}