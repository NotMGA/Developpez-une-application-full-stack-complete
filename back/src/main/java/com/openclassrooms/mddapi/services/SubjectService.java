package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import java.util.List;

public interface SubjectService {
    List<SubjectDTO> getAllSubjects();

    SubjectDTO getSubjectById(Long id);

    void subscribeToSubject(Long subjectId, Long userId);

    void unsubscribeFromSubject(Long subjectId, Long userId);

    List<SubjectDTO> getUserSubscriptions(Long userId);
}
