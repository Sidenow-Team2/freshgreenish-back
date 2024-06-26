package com.sidenow.freshgreenish.global.config.auth.dto;

import com.sidenow.freshgreenish.domain.user.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickname;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.picture = user.getFilePath();
    }
}