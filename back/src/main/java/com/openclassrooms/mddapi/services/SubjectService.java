package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing subjects, including retrieval, subscription, and
 * user-specific operations.
 */
@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a SubjectService with the required dependencies.
     *
     * @param subjectRepository the repository for subject-related database
     *                          operations.
     * @param userRepository    the repository for user-related database operations.
     */
    public SubjectService(SubjectRepository subjectRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all subjects and converts them to DTOs.
     *
     * @return a list of all subjects as DTOs.
     */
    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a subject by its ID and converts it to a DTO.
     *
     * @param id the ID of the subject to retrieve.
     * @return the subject as a DTO.
     * @throws RuntimeException if the subject is not found.
     */
    public SubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return convertToDTO(subject);
    }

    /**
     * Subscribes a user to a subject.
     *
     * @param subjectId the ID of the subject to subscribe to.
     * @param userId    the ID of the user subscribing to the subject.
     * @throws RuntimeException if the subject or user is not found, or if the user
     *                          is already subscribed.
     */
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

    /**
     * Unsubscribes a user from a subject.
     *
     * @param subjectId the ID of the subject to unsubscribe from.
     * @param userId    the ID of the user unsubscribing from the subject.
     * @throws RuntimeException if the subject or user is not found, or if the user
     *                          is not subscribed.
     */
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

    /**
     * Retrieves a list of subjects that a user is subscribed to and converts them
     * to DTOs.
     *
     * @param userId the ID of the user whose subscriptions are to be retrieved.
     * @return a list of subscribed subjects as DTOs.
     * @throws RuntimeException if the user is not found.
     */
    public List<SubjectDTO> getUserSubscriptions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getSubscriptions()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Subject entity to a SubjectDTO.
     *
     * @param subject the subject entity to convert.
     * @return the converted SubjectDTO.
     */
    private SubjectDTO convertToDTO(Subject subject) {
        return new SubjectDTO(subject.getId(), subject.getName(), subject.getDescription());
    }
}
