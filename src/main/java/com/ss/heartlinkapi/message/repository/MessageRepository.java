package com.ss.heartlinkapi.message.repository;

import com.ss.heartlinkapi.message.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query(value = "SELECT m.content FROM message m WHERE m.msg_room_id=:id ORDER BY m.created_at desc LIMIT 1", nativeQuery=true)
    String findByMsgRoomIdOrderByCreatedAt(@Param("id") Long id);
}
