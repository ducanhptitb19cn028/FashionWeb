package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.dto.Notification;
import com.anhnnd.fashionweb.model.Order;
import com.anhnnd.fashionweb.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for JSON conversion

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody Order order) {
        notificationService.sendNotification(order);
        return ResponseEntity.ok("Notification sent successfully");
    }
}
