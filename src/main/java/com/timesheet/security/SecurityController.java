package com.timesheet.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timesheet.models.ConnectedUser;
import com.timesheet.models.ConnectedUserModel;
import com.timesheet.services.AppUserService;

@RestController
@RequestMapping("/authentication")
public class SecurityController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtEncoder jwtEncoder;
	@Autowired
	private AppUserService appUserService;
		
	
	@PostMapping("/login")
	public Map<String, String>login(String username, String password){
		Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		Instant instant=Instant.now();
		String scope=authentication.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.joining(" "));	
		String employeeID=appUserService.getEmployeeID(username);
		JwtClaimsSet jwtClaimsSet=JwtClaimsSet.builder()
				.issuedAt(instant)
				.expiresAt(instant.plus(30, ChronoUnit.MINUTES))
				.subject(username)
				.claim("scope", scope)
				.claim("eid", employeeID)
				.build();		
		JwtEncoderParameters jwtEncoderParameters=JwtEncoderParameters.from(
			JwsHeader.with(MacAlgorithm.HS512).build(), jwtClaimsSet
		);		
		String jwt=jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
		ConnectedUserModel.addConnectedUser(new ConnectedUser(username, jwt, true));
		return Map.of("access-token",jwt); 
	}
	
}
