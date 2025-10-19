package com.amsidh.mvc.notificationservice.repository;

import com.amsidh.mvc.notificationservice.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
