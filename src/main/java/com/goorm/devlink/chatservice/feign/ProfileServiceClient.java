package com.goorm.devlink.chatservice.feign;


import com.goorm.devlink.chatservice.vo.response.ProfileSimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "profile-service")
public interface ProfileServiceClient {

    @GetMapping("/api/profile/chat")
    public ResponseEntity<List<ProfileSimpleResponse>> getProfileSimpleInfo(@RequestParam("userUuidList") List<String> userUuidList);
}
