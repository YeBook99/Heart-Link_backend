package com.ss.heartlinkapi.message.repository;

import com.ss.heartlinkapi.message.entity.MessageRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRoomRepository extends JpaRepository<MessageRoomEntity, Long> {
}
