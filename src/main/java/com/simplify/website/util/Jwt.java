package com.simplify.website.util;

import com.simplify.website.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class Jwt {
    private static final String segredo = "seuSegredoAqui";
    private static final long tempoExpiracao = 86400000; // 24h de autenticacao

    public static String gerarToken(Usuario usuario) {
        Date dataExpiracao = new Date(System.currentTimeMillis() + tempoExpiracao);

        return Jwts.builder()
                .setSubject(Integer.toString(usuario.getId()))
                .setIssuedAt(new Date())
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256, segredo)
                .compact();
    }

    public Integer extrairIdUsuario(String token) {
        return Integer.parseInt(Jwts.parser().setSigningKey(segredo).parseClaimsJws(token).getBody().getSubject());
    }
}
