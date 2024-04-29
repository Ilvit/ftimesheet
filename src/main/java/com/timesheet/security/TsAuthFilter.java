package com.timesheet.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.timesheet.models.ConnectedUserModel;

@Component
public class TsAuthFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest httpServletRequest=(HttpServletRequest)request;
		String accessToken=httpServletRequest.getHeader("Authorization");
		try {
			accessToken=accessToken.replace("Bearer ", "");
		} catch (Exception e) {
			System.out.println("he his not online");
		}
		
		if(ConnectedUserModel.isDisconnected(accessToken)) {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		chain.doFilter(request, response);
	}

}
