package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return convertToDTO(subject);
    }

    @Override
    public void subscribeToSubject(Long subjectId, Long userId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getSubscriptions().contains(subject)) {
            throw new RuntimeException("User is already subscribed to this subject");
        }

        user.getSubscriptions().add(subject);
        userRepository.save(user);
    }

    @Override
    public void unsubscribeFromSubject(Long subjectId, Long userId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getSubscriptions().contains(subject)) {
            throw new RuntimeException("User is not subscribed to this subject");
        }

        user.getSubscriptions().remove(subject);
        userRepository.save(user);
    }

    @Override
    public List<SubjectDTO> getUserSubscriptions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getSubscriptions()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SubjectDTO convertToDTO(Subject subject) {
        return new SubjectDTO(subject.getId(), subject.getName(), subject.getDescription());
    }
}
