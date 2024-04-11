package tz.go.samuel.danda.tutorials.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tz.go.samuel.danda.tutorials.model.Tutorial;
import tz.go.samuel.danda.tutorials.repository.TutorialRepository;
import tz.go.samuel.danda.tutorials.services.TutorialService;

@Service
public class TutorialServiceImpl implements TutorialService {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Override
    public Page<Tutorial> getTutorialByPublished(boolean published, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Tutorial> getTitleByContaining(String title, Pageable pageable) {
        return null;
    }
}
