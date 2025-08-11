package org.example.remotly_ecommerce.config;

import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RemotlyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // تحويل Authorities من Enum ل GrantedAuthority
        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                .filter(auth -> auth.getRole() != null) // التأكد إن الدور مش null
                .map(auth -> {
                    String name = auth.getRole().name(); // Enum → String
                    if (!name.startsWith("ROLE_")) {
                        name = "ROLE_" + name;
                    }
                    return new SimpleGrantedAuthority(name);
                })
                .collect(Collectors.toList());


        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
