package com.devhow.identity;

import com.devhow.identity.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserDataAccessTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void dataAccessTest() {
        assertThat(userRepository.count()).isGreaterThan(-1);
    }

}
