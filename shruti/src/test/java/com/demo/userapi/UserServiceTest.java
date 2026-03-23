package com.demo.userapi;

import com.demo.userapi.dto.UserDto;
import com.demo.userapi.exception.DuplicateEmailException;
import com.demo.userapi.exception.UserNotFoundException;
import com.demo.userapi.model.User;
import com.demo.userapi.repository.UserRepository;
import com.demo.userapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .name("Alice Smith")
                .email("alice@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllUsers_returnsListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<UserDto.Response> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void getUserById_existingId_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserDto.Response result = userService.getUserById(1L);

        assertThat(result.getName()).isEqualTo("Alice Smith");
    }

    @Test
    void getUserById_missingId_throwsNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createUser_newEmail_savesAndReturnsUser() {
        UserDto.CreateRequest request = new UserDto.CreateRequest();
        request.setName("Bob Jones");
        request.setEmail("bob@example.com");

        when(userRepository.existsByEmail("bob@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u = User.builder()
                    .id(2L).name(u.getName()).email(u.getEmail())
                    .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                    .build();
            return u;
        });

        UserDto.Response result = userService.createUser(request);

        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getEmail()).isEqualTo("bob@example.com");
    }

    @Test
    void createUser_duplicateEmail_throwsDuplicateException() {
        UserDto.CreateRequest request = new UserDto.CreateRequest();
        request.setName("Alice Clone");
        request.setEmail("alice@example.com");

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void deleteUser_existingId_deletesSuccessfully() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertThatNoException().isThrownBy(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_missingId_throwsNotFoundException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(UserNotFoundException.class);
    }
}