package com.example.demo.config;

import com.example.demo.entity.Account;
import com.example.demo.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;



    private final List<String> AUTH_PERMISSION = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/account/login",
            "/api/account/register",
            "/api/account/forgot-password",
            "/api/product",
            "/api/product/{id}",
            "/api/product/upload",
            "api/coffeeshops/**",
            "/api/orders/payment-callback/**"

    );

    public boolean checkIsPublicAPI(String uri){
        //uri : /api/register
        // neu gap cac uri tren => cho phep truy cap => token true
        AntPathMatcher patchMatch = new AntPathMatcher();
        //check token => false

        return AUTH_PERMISSION.stream().anyMatch(pattern -> patchMatch.match(pattern, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // ktra truoc khi cho phep truy cap vao controller
        boolean isPublicAPI = checkIsPublicAPI(request.getRequestURI());
        if (isPublicAPI) {
            filterChain.doFilter(request, response);
        } else {
            String token = getToken(request);
            if (token == null){ // token null => false
                resolver.resolveException(request, response, null, new AuthException("Token is required"));

                return;
            }

            Account account;
            // => check token co dung hay k => lay thong tin account tu token

            try {

               account = tokenService.getAccountByToken(token);
            }catch (ExpiredJwtException e) {
                resolver.resolveException(request, response, null, new AuthException("Token is expired"));
                // token het han
                return;
            }catch (MalformedJwtException malformedJwtException){
                resolver.resolveException(request, response, null, new AuthException("Token is invalid"));
               //token sai dinh dang.
                return;
            }
            // => token true => cho phep truy cap vao controller
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    account,
                    token,
                    account.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // token ok, cho vao`
            filterChain.doFilter(request, response);
        }



    }
    public String getToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return  null;
        return authHeader.substring(7);
    }


}
