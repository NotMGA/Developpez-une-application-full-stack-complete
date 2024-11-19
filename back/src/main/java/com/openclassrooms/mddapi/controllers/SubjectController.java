package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.services.SubjectService;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        SubjectDTO subjectDTO = subjectService.getSubjectById(id);
        return ResponseEntity.ok(subjectDTO);
    }

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Map<String, String>> subscribeToSubject(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Utilisateur non authentifié"));
        }

        User user = userService.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Utilisateur non trouvé"));
        }

        try {
            subjectService.subscribeToSubject(id, user.getId());
            return ResponseEntity.ok(Collections.singletonMap("message", "Vous êtes maintenant abonné au sujet !"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/unsubscribe")
    public ResponseEntity<Map<String, String>> unsubscribeFromSubject(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Utilisateur non authentifié"));
        }

        User user = userService.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Utilisateur non trouvé"));
        }

        try {
            subjectService.unsubscribeFromSubject(id, user.getId());
            return ResponseEntity.ok(Collections.singletonMap("message", "Vous êtes maintenant désabonné du sujet !"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubjectDTO>> getUserSubscriptions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        User user = userService.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        List<SubjectDTO> subscriptions = subjectService.getUserSubscriptions(user.getId());
        return ResponseEntity.ok(subscriptions);
    }
}
