package com.bai.euclsystem.services.impl;

import com.bai.euclsystem.dtos.requests.auth.LoginDTO;
import com.bai.euclsystem.dtos.response.auth.AuthResponse;
import com.bai.euclsystem.enums.IEmailTemplate;
import com.bai.euclsystem.exceptions.AppException;
import com.bai.euclsystem.models.User;
import com.bai.euclsystem.repositories.IUserRepository;
import com.bai.euclsystem.security.jwt.JwtUtils;
import com.bai.euclsystem.security.user.UserPrincipal;
import com.bai.euclsystem.services.IAuthService;
import com.bai.euclsystem.services.IUserService;
import com.bai.euclsystem.standalone.EmailService;
import com.bai.euclsystem.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthService {

    private final IUserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationProvider authenticationProvider;
    private final EmailService emailService;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;



    @Override
    public AuthResponse login(LoginDTO loginDTO) {
        Authentication authentication = authenticateUser(loginDTO);
        return generateJwtAuthenticationResponse(authentication);
    }

    private Authentication authenticateUser(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        Authentication authentication = authenticationProvider.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
    private AuthResponse generateJwtAuthenticationResponse(Authentication authentication) {
        String jwt = jwtUtils.generateAccessToken(authentication);
        UserPrincipal userPrincipal = UserUtils.getLoggedInUser();
        assert userPrincipal != null;
        User user = userService.findUserById(userPrincipal.getId());
        user.setFullName(user.getFirstName() + " " + user.getLastName());
        return new AuthResponse(jwt, user);
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new AppException(String.format("User with email %s not found", email))
        );

        String resetCode = UserUtils.generateToken();
        user.setPasswordResetCode(resetCode);
        user.setPasswordResetCodeGeneratedAt(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> variables = new HashMap<>();
        variables.put("code", resetCode);

        try {
            emailService.sendEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    "Reset your password",
                    IEmailTemplate.RESET_PASSWORD,
                    variables
            );
        } catch (Exception e) {
            throw new AppException("Failed to send reset password email: " + e.getMessage());
        }
    }


    @Override
    public void resetPassword(String email, String passwordResetCode, String newPassword) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new AppException(String.format("User with email %s not found", email))
        );

        if (!passwordResetCode.equals(user.getPasswordResetCode())) {
            throw new AppException("Invalid password reset code");
        }

        if (user.getPasswordResetCodeGeneratedAt() == null ||
                user.getPasswordResetCodeGeneratedAt().isBefore(LocalDateTime.now().minusMinutes(15))) {
            throw new AppException("Password reset code has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetCode(null);
        user.setPasswordResetCodeGeneratedAt(null);
        userRepository.save(user);
    }


    @Override
    public void initiateAccountVerificaton(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new AppException(String.format("User with email %s not found", email))
        );

        if(user.isVerified()) return;

        String verificationCode = UserUtils.generateToken();
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeGeneratedAt(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> variables = new HashMap<>();
        variables.put("code", verificationCode);

        try{
            emailService.sendEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    "Verify your account",
                    IEmailTemplate.ACCOUNT_VERIFICATION,
                    variables
            );
        } catch (Exception e){
            throw new AppException("Failed to send verification email: " + e.getMessage());
        }
    }

    @Override
    public void verifyAccount(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode).orElseThrow(
                () -> new AppException("Invalid verification code")
        );

        Duration codeValidity = Duration.ofMinutes(15);
        if (user.getVerificationCodeGeneratedAt() == null ||
                user.getVerificationCodeGeneratedAt().isBefore(LocalDateTime.now().minus(codeValidity))) {
            throw new AppException("Verification code has expired");
        }


        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeGeneratedAt(null);
        userRepository.save(user);
    }


    @Override
    public void resendVerificationCode(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new AppException("User not found")
        );

        if (user.isVerified()) return;

        String newCode = UserUtils.generateToken();
        user.setVerificationCode(newCode);
        userRepository.save(user);

        Map<String, Object> variables = new HashMap<>();
        variables.put("code", newCode);

        try {
            emailService.sendEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    "Resend Account Verification",
                    IEmailTemplate.ACCOUNT_VERIFICATION,
                    variables
            );
        } catch (Exception e) {
            throw new AppException("Failed to resend verification email: " + e.getMessage());
        }
    }

    @Override
    public void updatePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new AppException("User not found")
        );

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AppException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }



}
