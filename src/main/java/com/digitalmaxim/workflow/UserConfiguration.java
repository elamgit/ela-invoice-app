package com.digitalmaxim.workflow;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.IdentityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Profile("prod")
@Configuration
public class UserConfiguration {

    @Autowired
    private ProcessEngine engine;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserConfiguration.class);


    @PostConstruct
    public void addUsers() {

        // logic copied from
        // https://github.com/camunda/camunda-bpm-platform/blob/master/examples/invoice/src/main/java/org/camunda/bpm/example/invoice/DemoDataGenerator.java

        final IdentityServiceImpl identityService = (IdentityServiceImpl) engine.getIdentityService();

        if (identityService.isReadOnly()) {
            LOGGER.info("Identity service provider is Read Only, not creating any demo users.");
            return;
        }

        User singleResult = identityService.createUserQuery().userId("user1").singleResult();
        if (singleResult != null) {
            LOGGER.info("User1 already added. Skipping");
        } else {
            LOGGER.info("adding user1");
            User user = identityService.newUser("user1");
            user.setFirstName("first1");
            user.setLastName("last1");
            user.setPassword("pass1");
            user.setEmail("user1@camunda.org");
            identityService.saveUser(user, true);
        }
    }

}
