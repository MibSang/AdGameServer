package com.fufumasi.AdGameServer.filters;

import com.fufumasi.AdGameServer.controllers.tokenHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class jwtAuthFilter extends OncePerRequestFilter {
    @Inject
    private tokenHandler tokenhandler;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!request.getRequestURI().equals("/login")) { // login 페이지는 token 필터에서 제외
            try {
                String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                Claims claims = tokenhandler.parseJwtToken(authorizationHeader);
                request.setAttribute("claims", claims);
            } catch (IllegalArgumentException e) {
                System.out.println("IllegalArgumentException");
                response.sendError(401);
                return;
            } catch (ExpiredJwtException e) {
                System.out.println("ExpiredJwt");
                response.sendRedirect("/logout");
                return;
            } catch (Exception e) {
                System.out.println("Exception");
                response.sendError(401);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }
}