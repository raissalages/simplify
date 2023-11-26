package com.simplify.website.service;
import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessaoService {
    private MapSessionRepository sessaoRepository;


    public String iniciarSessao(String email) {
        String sessionId = UUID.randomUUID().toString();
        MapSession session = new MapSession(sessionId);
        session.setAttribute("email", email);
        sessaoRepository.save(session);
        return sessionId;
    }

    public String getEmailDoUsuario(String sessionId) {
        MapSession session = sessaoRepository.findById(sessionId);
        return (session != null) ? (String) session.getAttribute("email") : null;
    }
}
