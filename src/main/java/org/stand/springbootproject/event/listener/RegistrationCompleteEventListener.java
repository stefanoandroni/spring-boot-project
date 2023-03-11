package org.stand.springbootproject.event.listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.stand.springbootproject.entity.user.User;
import org.stand.springbootproject.event.RegistrationCompleteEvent;
import org.stand.springbootproject.service.UserService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final Logger LOG = LoggerFactory.getLogger(RegistrationCompleteEventListener.class);
    private final UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // LOG.info("IN onApplicationEvent(event)");

        // Generate and save a new verification token for the user
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);

        //Send email to User
        String url = event.getApplicationUrl() + "validate?token=" + token;
        LOG.info("Click in link to verify your account: {}", url); // TODO: for now simulated
        // ...
    }
}
