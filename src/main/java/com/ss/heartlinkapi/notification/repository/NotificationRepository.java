package com.ss.heartlinkapi.notification.repository;

import com.ss.heartlinkapi.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
