package org.forum.service;

import org.forum.entities.user.UserProfile;
import org.springframework.stereotype.Service;

@Service
public interface UserProfileService {

    UserProfile findOne(int uzivatelId);

    UserProfile findOne(String uzivatelskeMeno);
}
