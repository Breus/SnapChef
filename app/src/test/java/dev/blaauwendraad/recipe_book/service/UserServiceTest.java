package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.exception.UserAuthenticationException;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.ws.rs.NotFoundException;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String TEST_USER = "testuser";
    private static final String TEST_PASSWORD = "Password123";
    private UserRepository userRepository;
    private UserService userService;

    @BeforeAll
    static void setup() {
        Assertions.setMaxStackTraceElementsDisplayed(500); // or any number you want
    }

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void getUserTest() {
        // Given
        UserAccountEntity userAccountEntity = createUserAccountEntity(1L, "testuser@example.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(userAccountEntity);

        // When
        UserAccount userAccount = userService.getUser(1L);

        // Then
        Assertions.assertThat(userAccount.id()).isEqualTo(1L);
        Assertions.assertThat(userAccount.username()).isEqualTo("testuser");
        Assertions.assertThat(userAccount.emailAddress()).isEqualTo("testuser@example.com");
        Assertions.assertThat(BcryptUtil.matches(TEST_PASSWORD, userAccount.passwordHash()))
                .isTrue();
    }

    @Test
    void getUserTest_NotFound() {
        Assertions.assertThatThrownBy(() -> userService.getUser(2L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with given userId does not exist");
    }

    @Test
    void updateEmail() throws Exception {
        // Given
        UserAccountEntity userAccountEntity = createUserAccountEntity(1L, "testuser@example.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(userAccountEntity);

        // When
        userService.updateEmail(1L, "newemail@example.com", TEST_PASSWORD);

        // Then
        Mockito.verify(userRepository).persist(userAccountEntity);
        Assertions.assertThat(userAccountEntity.emailAddress).isEqualTo("newemail@example.com");
    }

    @Test
    void updateEmail_NotFound() {
        Assertions.assertThatThrownBy(() -> userService.updateEmail(10L, "newemail@example.com", TEST_PASSWORD))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with given userId does not exist");
    }

    @Test
    void updateEmail_InvalidPassword() {
        // Given
        UserAccountEntity userAccountEntity = createUserAccountEntity(1L, "testuser@example.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(userAccountEntity);

        // When, Then
        Assertions.assertThatThrownBy(() -> userService.updateEmail(1L, "newemail@example.com", "WRONG_PASSWORD"))
                .isInstanceOf(UserAuthenticationException.class)
                .hasMessageContaining("Failed to authenticate user.")
                .hasFieldOrPropertyWithValue("detailMessage", "Invalid password provided.");
    }

    @Test
    void updateEmail_EmailAlreadyInUse() {
        // Given
        String EXISTING_EMAIL = "existingemail@example.com";
        UserAccountEntity userAccountEntity = createUserAccountEntity(1L, "testuser@example.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(userAccountEntity);
        Mockito.when(userRepository.findByEmail(EXISTING_EMAIL))
                .thenReturn(createUserAccountEntity(2L, EXISTING_EMAIL));

        // When
        Assertions.assertThatThrownBy(() -> userService.updateEmail(1L, EXISTING_EMAIL, TEST_PASSWORD))
                .isInstanceOf(UserAuthenticationException.class)
                .hasMessageContaining("Failed to authenticate user.")
                .hasFieldOrPropertyWithValue("detailMessage", "The provided new email address is already in use.");
    }

    @Test
    void updatePassword() throws Exception {
        // Given
        String NEW_PASSWORD = "newPassword123";
        UserAccountEntity userAccountEntity = createUserAccountEntity(1L, "testuser@example.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(userAccountEntity);

        // When
        userService.updatePassword(1L, TEST_PASSWORD, NEW_PASSWORD);

        // Then
        Mockito.verify(userRepository).persist(userAccountEntity);
        Assertions.assertThat(BcryptUtil.matches(NEW_PASSWORD, userAccountEntity.passwordHash))
                .isTrue();
    }

    @Test
    void updatePassword_NotFound() {
        Assertions.assertThatThrownBy(() -> userService.updatePassword(10L, TEST_PASSWORD, "newpassword123"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with given userId does not exist");
    }

    @Test
    void updatePassword_InvalidCurrentPassword() {
        // Given
        UserAccountEntity userAccountEntity = createUserAccountEntity(1L, "testuser@example.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(userAccountEntity);

        // When, Then
        Assertions.assertThatThrownBy(() -> userService.updatePassword(1L, "WRONG_PASSWORD", "newPassword123"))
                .isInstanceOf(UserAuthenticationException.class)
                .hasMessageContaining("Failed to authenticate user.")
                .hasFieldOrPropertyWithValue("detailMessage", "Current password is incorrect.");
    }

    private UserAccountEntity createUserAccountEntity(Long userId, String email) {
        var userAccountEntity = new UserAccountEntity();
        userAccountEntity.id = userId;
        userAccountEntity.username = TEST_USER;
        userAccountEntity.emailAddress = email;
        userAccountEntity.passwordHash = BcryptUtil.bcryptHash(TEST_PASSWORD);
        userAccountEntity.roles = Set.of();
        return userAccountEntity;
    }
}
