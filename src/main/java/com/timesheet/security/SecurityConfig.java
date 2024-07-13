package com.timesheet.security;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	private String secreteKey="ieur@Mkfhei125gt4e7rldsioeèf@skédp^^dksefke1e35M5dmerlfjfkvkrtui";

	@Autowired
	private TsAuthFilter tsAuthFilter;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private UserDetailService appUserDetailService;
	
/*	@Bean
	InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//		PasswordEncoder passwordEncoder=passwordEncoder();
		return new InMemoryUserDetailsManager(
				User.withUsername("user1").password(passwordEncoder.encode("12345")).authorities("USER").build(),
				User.withUsername("admin").password(passwordEncoder.encode("12345")).authorities("USER","CAR_MANAGER").build(),
				User.withUsername("admin2").password(passwordEncoder.encode("12345")).authorities("USER","ADMIN","CAR_MANAGER").build(),
				User.withUsername("admin3").password(passwordEncoder.encode("12345")).authorities("USER","ADMIN","USER_MANAGER","CAR_MANAGER").build()
		); 		
	}
//*/	
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        
        return httpSecurity.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        		.csrf(csrf->csrf.disable())
        		.cors(Customizer.withDefaults())
        		.authorizeHttpRequests(hr->hr.requestMatchers("/authentication/login/**").permitAll())
        		.authorizeHttpRequests(hr->hr.requestMatchers("http://localhost:8081/h2-console/**").permitAll())
        		.addFilterBefore(tsAuthFilter, UsernamePasswordAuthenticationFilter.class)
        		.authorizeHttpRequests(hr->hr.anyRequest().authenticated())
//        		.httpBasic(Customizer.withDefaults())        		
        		.oauth2ResourceServer(oa->oa.jwt(Customizer.withDefaults()))
        		.userDetailsService(appUserDetailService)
        		.build();
      
	}
    @Bean
    JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(secreteKey.getBytes()));    	
    }
    @Bean
    JwtDecoder jwtDecoder() {
    	SecretKeySpec secretKeySpec=new SecretKeySpec(secreteKey.getBytes(), "RSA");
    	return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }
    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
    	DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
    	authenticationProvider.setPasswordEncoder(passwordEncoder);
    	authenticationProvider.setUserDetailsService(userDetailsService);
    	return new ProviderManager(authenticationProvider);
    }
    @Bean 
    CorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration corsConfiguration=new CorsConfiguration();
    	corsConfiguration.addAllowedOrigin("*");
    	corsConfiguration.addAllowedMethod("*");
    	corsConfiguration.addAllowedHeader("*");
    	UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", corsConfiguration);
    	return source ; 
    }
}
