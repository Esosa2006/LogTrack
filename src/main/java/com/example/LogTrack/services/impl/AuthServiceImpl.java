package com.example.LogTrack.services.impl;

import com.example.LogTrack.enums.Role;
import com.example.LogTrack.enums.TokenType;
import com.example.LogTrack.exceptions.exceptions.AuthenticationFailedException;
import com.example.LogTrack.exceptions.exceptions.EmailAlreadyInUseException;
import com.example.LogTrack.exceptions.exceptions.GlobalException;
import com.example.LogTrack.exceptions.exceptions.PasswordMissmatchException;
import com.example.LogTrack.models.dtos.authDtos.LoginDto;
import com.example.LogTrack.models.dtos.authDtos.ResetPasswordDto;
import com.example.LogTrack.models.dtos.authDtos.StudentSignUpRequest;
import com.example.LogTrack.models.dtos.authDtos.SupervisorSignUpRequest;
import com.example.LogTrack.models.entities.AppUser;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.Supervisor;
import com.example.LogTrack.models.entities.Token;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.SupervisorRepository;
import com.example.LogTrack.repositories.TokenRepository;
import com.example.LogTrack.security.JWTService;
import com.example.LogTrack.services.AuthService;
import com.example.LogTrack.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final StudentRepository studentRepository;
    private final SupervisorRepository supervisorRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthServiceImpl(StudentRepository studentRepository, SupervisorRepository supervisorRepository, AuthenticationManager authenticationManager, JWTService jwtService, EmailService emailService, TokenRepository tokenRepository) {
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public ResponseEntity<String> studentSignUp(StudentSignUpRequest signUpRequest) {
        Student student = studentRepository.findByEmail(signUpRequest.getEmail());
        if (student != null) {
            log.error("Email found in student Repo! Student already exists");
            throw new EmailAlreadyInUseException("This email is already registered");
        }
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            log.error("Passwords and Confirm Password do not match!");
            throw new PasswordMissmatchException("Passwords do not match");
        }
        Student newStudent = createStudent(signUpRequest);
        studentRepository.save(newStudent);
        log.info("New Student Created");
        verifyAccountTokenMessage(newStudent);
        return ResponseEntity.status(HttpStatus.CREATED).body("Student Registration successful!");
    }

    @Override
    public ResponseEntity<String> supervisorSignUp(SupervisorSignUpRequest supervisorSignUpRequest) {
        Supervisor supervisor = supervisorRepository.findByEmail(supervisorSignUpRequest.getEmail());
        if (supervisor != null) {
            log.error("Email found in supervisor Repo! Supervisor already exists");
            throw new EmailAlreadyInUseException("This email is already registered");
        }
        if (!supervisorSignUpRequest.getPassword().equals(supervisorSignUpRequest.getConfirmPassword())) {
            log.error("Passwords do not match!");
            throw new PasswordMissmatchException("Passwords do not match");
        }
        Supervisor newSupervisor = createSupervisor(supervisorSignUpRequest);
        supervisorRepository.save(newSupervisor);
        log.info("New Supervisor Created");
        verifyAccountTokenMessage(newSupervisor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Supervisor Registration successful!");
    }

    @Override
    public ResponseEntity<String> login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            if (!authentication.isAuthenticated()) {
                log.error("Invalid username or password!");
                throw new AuthenticationFailedException("Authentication failed");
            }

            log.info("Authentication successful!");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(jwtService.generateToken(loginDto.getEmail()));

        } catch (DisabledException ex) {
            log.error("Account is disabled!");
            throw new AuthenticationFailedException("Your account is disabled. Please contact support.");
        }
    }

    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        Token userToken = tokenRepository.findTokenByToken(token);
        tokenChecks(userToken);
        AppUser appUser = userToken.getUser();
        if (appUser == null) {
            throw new GlobalException("No user linked to this token");
        }

        appUser.setEnabled(true);
        userToken.setUsed(true);

        if (appUser instanceof Student student) {
            studentRepository.save(student);
            studentWelcomeMessage(student);
        }
        if (appUser instanceof Supervisor supervisor) {
            supervisorRepository.save(supervisor);
            supervisorWelcomeMessage(supervisor);
        }

        return ResponseEntity.ok("Account verified! You can now log in.");
    }


    @Override
    public ResponseEntity<String> resetPassword(String email) {
        if(email == null) {
            throw new GlobalException("No email was entered!");
        }
        Student student = studentRepository.findByEmail(email);
        if (student != null) {
            addPasswordResetToken(student);
            studentRepository.save(student);
            resetPasswordMessage(student);
            return ResponseEntity.status(HttpStatus.OK).body("Reset Password Email Sent!");
        }
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if (supervisor != null) {
            addPasswordResetToken(supervisor);
            supervisorRepository.save(supervisor);
            resetPasswordMessage(supervisor);
            return ResponseEntity.status(HttpStatus.OK).body("Reset Password Email Sent!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email!");
    }

    @Override
    public ResponseEntity<String> verifyResetPasswordToken(String token, ResetPasswordDto resetPasswordDto) {
        Token userToken =  tokenRepository.findTokenByToken(token);
        tokenChecks(userToken);
        AppUser appUser = userToken.getUser();
        boolean passwordMatch = resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmNewPassword());
        if (passwordMatch) {
            if(appUser instanceof Student student) {
                userToken.setUsed(true);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                student.setPassword(encoder.encode(resetPasswordDto.getNewPassword()));
                studentRepository.save(student);
                return ResponseEntity.status(HttpStatus.OK).body("Password Successfully Reset!");
            }
            else if (appUser instanceof Supervisor supervisor) {
                userToken.setUsed(true);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                supervisor.setPassword(encoder.encode(resetPasswordDto.getNewPassword()));
                supervisorRepository.save(supervisor);
                return ResponseEntity.status(HttpStatus.OK).body("Password Successfully Reset!");
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match!");
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token!");
        }
    }


    private static Student createStudent(StudentSignUpRequest studentSignUpRequest) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Student newStudent = new Student();
        newStudent.setEmail(studentSignUpRequest.getEmail());
        newStudent.setMatricNumber(studentSignUpRequest.getMatricNumber());
        newStudent.setPassword(encoder.encode(studentSignUpRequest.getPassword()));
        newStudent.setName(studentSignUpRequest.getName());
        newStudent.setRole(Role.STUDENT);
        newStudent.setCreatedAt(new Date());
        newStudent.setEnabled(false);
        Token token = new Token();
        token.setTokenType(TokenType.ACCOUNT_ACTIVATION);
        token.setExpiry(LocalDateTime.now().plusHours(24));
        token.setUser(newStudent);
        newStudent.getTokens().add(token);
        return newStudent;
    }

    private static Supervisor createSupervisor(SupervisorSignUpRequest supervisorSignUpRequest) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Supervisor newSupervisor = new Supervisor();
        newSupervisor.setEmail(supervisorSignUpRequest.getEmail());
        newSupervisor.setPassword(encoder.encode(supervisorSignUpRequest.getPassword()));
        newSupervisor.setName(supervisorSignUpRequest.getName());
        newSupervisor.setRole(Role.SUPERVISOR);
        newSupervisor.setCreatedAt(new Date());
        newSupervisor.setEnabled(false);
        Token token = new Token();
        token.setTokenType(TokenType.ACCOUNT_ACTIVATION);
        token.setExpiry(LocalDateTime.now().plusHours(24));
        token.setUser(newSupervisor);
        newSupervisor.getTokens().add(token);
        return newSupervisor;
    }

    private void supervisorWelcomeMessage(Supervisor supervisor) {
        emailService.sendEmail(supervisor.getEmail(),
                "Welcome to LogTrack!",
                """
                        Your account has been successfully created.
                        
                        You can now monitor and evaluate your students daily/weekly log entries and track their internship progress.\
                        If you did not sign up for this account, please contact the system administrator immediately.
                        
                        Best regards, \s
                        The Log-track Team""");
    }
    private void studentWelcomeMessage(Student student) {
        emailService.sendEmail(student.getEmail(),
                "Welcome to LogTrack!",
                """
                        Your account has been successfully created.
                        
                        You can now log in to submit your daily/weekly log entries and track your internship progress.\
                        If you did not sign up for this account, please contact the system administrator immediately.
                        
                        Best regards, \s
                        The Log-track Team""");
    }

    private void resetPasswordMessage(AppUser appUser) {
        Token resetToken = appUser.getTokens().stream()
                .filter(token -> token.getTokenType() == TokenType.PASSWORD_RESET && !token.isUsed())
                .findFirst()
                .orElseThrow(() -> new GlobalException("No active reset token found"));

        emailService.sendEmail(
                appUser.getEmail(),
                "Reset Your Password",
                "Reset Token : " + resetToken.getToken()
        );
    }

    private void verifyAccountTokenMessage(AppUser appUser) {
        Token resetToken = appUser.getTokens().stream()
                .filter(token -> token.getTokenType() == TokenType.ACCOUNT_ACTIVATION && !token.isUsed())
                .findFirst()
                .orElseThrow(() -> new GlobalException("No active account activation token found"));

        emailService.sendEmail(
                appUser.getEmail(),
                "Activate your LogTrack account",
                "Activation Token : " + resetToken.getToken()
        );
    }

    private void addPasswordResetToken(AppUser appUser) {
        Token token = new Token();
        token.setTokenType(TokenType.PASSWORD_RESET);
        token.setExpiry(LocalDateTime.now().plusMinutes(10));
        token.setUser(appUser);
        appUser.getTokens().add(token);
    }

    private void tokenChecks(Token userToken){
        if (userToken == null) {
            throw new GlobalException("Invalid token");
        }

        if (userToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new GlobalException("Token has expired");
        }

        if (userToken.isUsed()) {
            throw new GlobalException("Token has already been used");
        }
    }
}
