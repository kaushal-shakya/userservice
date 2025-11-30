package com.kaushal.userservice.security;

import com.kaushal.userservice.repositories.AccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Incoming request - {} " ,request.getRequestURI());
        try{
            final String authHeader = request.getHeader("Authorization");

            if( authHeader == null || !authHeader.startsWith("Bearer ") ) {
                log.warn("Missing or invalid Authorization header");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            System.out.println("Extracted Token: " + token);

            String email = authUtil.getEmailFromToken(token);
            System.out.println("Extracted Email: " + email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                Optional user = accountRepository.findByEmail(email);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, null, null);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            filterChain.doFilter(request, response);
        }catch (Exception e){
            log.error("Error in JwtAuthFilter: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request,response,null,e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }
}
