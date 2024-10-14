package com.ss.heartlinkapi.message.repository;

import com.ss.heartlinkapi.message.dto.MessageRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRoomRepository extends JpaRepository<MessageRoomEntity, Long> {
}
