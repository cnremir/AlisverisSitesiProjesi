package com.alisveris.AlisverisSitesi.security;


import com.alisveris.AlisverisSitesi.services.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler; // Success handler


    public SecurityConfig(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcRequestBuilder = new MvcRequestMatcher.Builder(introspector); //şuna bak tekrar


        security
                .headers(x->x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(csrfConfig->
                        csrfConfig.ignoringRequestMatchers(mvcRequestBuilder.pattern("/public/**"))
                                .ignoringRequestMatchers(PathRequest.toH2Console()))

                .formLogin(x->
                        x.loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/profilePage",true)
                                .successHandler(customAuthenticationSuccessHandler)
                                .failureUrl("/login?error=true")
                                .permitAll())
                .authorizeHttpRequests(
                        x-> x

                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // .requestMatchers(mvcRequestBuilder.pattern("/login/**")).permitAll()
                        // .requestMatchers(mvcRequestBuilder.pattern("/private/**")).hasAnyRole(Role.ROLE_USER.getValue(),Role.ROLE_ADMIN.getValue())

                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Çıkış işlemi
                        .logoutSuccessUrl("/login?logout=true") // Çıkış sonrası yönlendirme
                        .permitAll()
                )

                .httpBasic(Customizer.withDefaults());
        return security.build();
    }





}
