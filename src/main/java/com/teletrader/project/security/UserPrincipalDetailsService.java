package com.teletrader.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.teletrader.project.entities.User;
import com.teletrader.project.repositories.UserJpaRepository;




@Service
public class UserPrincipalDetailsService implements UserDetailsService {
	
	@Autowired
	UserJpaRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.userRepository.findById(s).get();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        return userPrincipal;
    }
}
