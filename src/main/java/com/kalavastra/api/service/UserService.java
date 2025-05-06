package com.kalavastra.api.service;

import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.User;
import com.kalavastra.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;

    @Transactional
    public User create(User u) {
        u.setIsActive(true);
        return repo.save(u);
    }

    @Transactional(readOnly=true)
    public User getByUserId(String userId) {
        return repo.findByUserId(userId)
                   .orElseThrow(() -> new ResourceNotFoundException("User","userId",userId));
    }

    @Transactional
    public User update(String userId, User req) {
        User u = getByUserId(userId);
        u.setName(req.getName());
        u.setPhoneNumber(req.getPhoneNumber());
        // etc...
        return repo.save(u);
    }
}
