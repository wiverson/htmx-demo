package com.devhow.identity;

import com.devhow.identity.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDataAccessTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void dataAccessTest() {
        assertThat(userRepository.count()).isGreaterThan(-1);
    }

}
