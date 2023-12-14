package com.example.chattingservice.repository;

import com.example.chattingservice.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomUserRepository extends JpaRepository<RoomUser,String>, RoomUserRepositoryCustom {
}
