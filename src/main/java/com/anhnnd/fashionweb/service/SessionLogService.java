package com.anhnnd.fashionweb.service;

import com.anhnnd.fashionweb.model.SessionLog;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.repository.SessionLogRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SessionLogService {
    @Autowired
    private SessionLogRepository sessionLogRepository;


    @Transactional
    public void addSessionLog(@org.jetbrains.annotations.NotNull HttpSession session, User user, String pageaccess) {
        SessionLog sessionLog = new SessionLog();
        sessionLog.setUser(user);
        sessionLog.setSession(session.getId());
        sessionLog.setPageAccessed(pageaccess);
        sessionLog.setLoginTime(LocalDateTime.now());
        sessionLogRepository.save(sessionLog);
    }
}
