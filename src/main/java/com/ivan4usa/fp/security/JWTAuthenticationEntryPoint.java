//package com.ivan4usa.fp.security;
//
//import com.google.gson.Gson;
//import com.ivan4usa.fp.payload.response.InvalidLoginResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//        String jsonLoginResponse = new Gson().toJson(new InvalidLoginResponse());
//        httpServletResponse.setContentType(SecurityConstants.CONTENT_TYPE);
//        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
//        httpServletResponse.getWriter().println(jsonLoginResponse);
//    }
//}
