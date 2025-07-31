package com.example.LogTrack.security;

import com.example.LogTrack.models.entities.Admin;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.Supervisor;
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

    @Autowired
    public MyUserDetailsService(StudentRepository studentRepository, SupervisorRepository supervisorRepository) {
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(email);
        if(student != null){
            return new UserPrincipal(student.getEmail(), student.getPassword(),student.getRole());
        }
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if(supervisor != null){
            return new UserPrincipal(supervisor.getEmail(), supervisor.getPassword(),supervisor.getRole());
        }
        throw new RuntimeException("User not found!");
    }
}
