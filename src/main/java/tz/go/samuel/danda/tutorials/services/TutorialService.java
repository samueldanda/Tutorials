package tz.go.samuel.danda.tutorials.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tz.go.samuel.danda.tutorials.model.Tutorial;

public interface TutorialService {
    Page<Tutorial> getTutorialByPublished(boolean published, Pageable pageable);
    Page<Tutorial> getTitleByContaining(String title, Pageable pageable);
}
