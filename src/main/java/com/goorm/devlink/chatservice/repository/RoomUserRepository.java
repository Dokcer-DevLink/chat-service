package com.goorm.devlink.chatservice.repository;

import com.goorm.devlink.chatservice.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomUserRepository extends JpaRepository<RoomUser,String>, RoomUserRepositoryCustom {
}
