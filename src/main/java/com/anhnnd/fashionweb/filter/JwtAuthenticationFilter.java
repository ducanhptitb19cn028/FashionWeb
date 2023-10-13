package com.anhnnd.fashionweb.filter;



import com.anhnnd.fashionweb.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final UserDetailsService userSetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwt;
        String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        userEmail = null;
        jwt = authHeader.substring(7);
        try {
            if (googleSign(jwt) != null) {
                userEmail = googleSign(jwt);
                UserDetails userDetails = userSetailsService.loadUserByUsername(userEmail);
                jwt = jwtService.generateToken(userDetails);
            }
        }
        catch (Exception e) {

        }
        if (userEmail == null){
            userEmail = jwtService.extractUsername(jwt);
        }
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userSetailsService.loadUserByUsername(userEmail);
            if (userDetails != null && jwtService.isTokenValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
    private String googleSign(String token){
        try {
            URL url = new URL("https://oauth2.googleapis.com/tokeninfo?id_token=" + token);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();
            JSONObject jsonObj = new JSONObject(response.toString());
            System.out.println(jsonObj);
            String email = jsonObj.getString("email");
            return email;
        }
        catch (Exception e) {
            return null;
        }
    }
}
