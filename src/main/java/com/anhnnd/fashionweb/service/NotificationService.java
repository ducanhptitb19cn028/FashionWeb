package com.anhnnd.fashionweb.service;


import com.anhnnd.fashionweb.dto.Notification;
import com.anhnnd.fashionweb.model.Order;
import com.anhnnd.fashionweb.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    public void sendNotification(Order order) {
        Notification notification = new Notification();
        // Logic to send notification
        String message = "You have 1 order with ID " + order.getId() + " from " + order.getUser().getEmail();
        notification.setMessage(message);
        notification.setTitle("New order notification");
        notificationRepository.save(notification);
    }
}
