//package com.Khushi.recommendation_backend.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//
//@Service
//public class JWTService {
//    private final String SECRET="mysecretkeymysecretkeymysecretkey";
//    private Key getSignKey(){
//        return Keys.hmacShaKeyFor(SECRET.getBytes());
//    }
//    public String generateToken(String email){
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))//1 hour
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//    public String extractEmail(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(getSignKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//    public boolean validateToken(String token){
//        try{
//            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
