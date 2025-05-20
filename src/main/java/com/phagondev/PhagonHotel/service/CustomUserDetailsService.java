package com.phagondev.PhagonHotel.service;

import com.phagondev.PhagonHotel.exception.OurException;
import com.phagondev.PhagonHotel.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepo.findByEmail(username)
               .orElseThrow(()-> new OurException("Username/Email not found"));

    }
}
