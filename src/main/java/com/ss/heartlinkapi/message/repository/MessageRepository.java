package com.ss.heartlinkapi.message.repository;

import com.ss.heartlinkapi.message.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
