package com.keduit.shop.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    System.out.println("-------------------------- securityFilterChain -----------------");
    /*usernameParameter가 하이라이트임 loadUserByUsername의(String email)매개변수로들어갈내용이 들어가야함*/
    http.formLogin().loginPage("/members/login").defaultSuccessUrl("/").usernameParameter("email")
            .failureUrl("/members/login/error").and().logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")).logoutSuccessUrl("/");

    /*여기가 핵심적인부분임*/
    /*permitAll() : 모든 사용자가 인증(로그인) 없이 해당 경로에 접근 가능 */
    /*hasRole("ADMIN") : 관리자인 경우 접근가능페이지가 admin의 모든페이지 라는것*/
    /*anyRequest().authenticated(); : 위의 경우 이외의 페이지는 인증절차가 필요하다는것*/

    http.authorizeRequests().mvcMatchers("/", "members/**", "/item/**", "/images/**","error","favicon.io").permitAll().mvcMatchers("/admin/**")
            .hasRole("ADMIN").anyRequest().authenticated();

    /*인증되지 않은 사용자가 리소스 접근 하여 실패 했을때 처리하는 핸들러 등록 */
    http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  /*resource/static 폴더의 하위 파일은 인증에서 제외시키기*/
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }
}
