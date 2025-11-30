package com.ktbweek4.community.user.service;

import com.ktbweek4.community.user.entity.User;
import com.ktbweek4.community.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    void testFindUserById() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .username("testUser")
                .password("1234")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = userService.findUserById(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testUser");
    }
}