package com.example.LogTrack.security;

import com.example.LogTrack.models.entities.Admin;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.Supervisor;
import com.example.LogTrack.repositories.AdminRepository;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final SupervisorRepository supervisorRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public MyUserDetailsService(StudentRepository studentRepository, SupervisorRepository supervisorRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(email);
        if(student != null){
            return new UserPrincipal(student.getEmail(), student.getPassword(),student.getRole(), student.isEnabled());
        }
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if(supervisor != null){
            return new UserPrincipal(supervisor.getEmail(), supervisor.getPassword(),supervisor.getRole(), supervisor.isEnabled());
        }
        Admin admin = adminRepository.findByEmail(email);
        if(admin != null){
            return new UserPrincipal(admin.getEmail(), admin.getPassword(), admin.getRole(), admin.isEnabled());
        }
        throw new RuntimeException("User not found!");
    }
}
