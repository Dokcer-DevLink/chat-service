package com.goorm.devlink.chatservice.vo.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProfileSimpleResponse {
    private String userUuid;
    private String profileImageUrl;
    private String nickname;

    public static ProfileSimpleResponse getInstance(){
        return new ProfileSimpleResponse();
    }
}
