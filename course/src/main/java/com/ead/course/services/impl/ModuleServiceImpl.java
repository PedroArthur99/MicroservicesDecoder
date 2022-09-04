package com.ead.course.services.impl;

import com.ead.course.controllers.model.LessonModel;
import com.ead.course.controllers.model.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository repository;

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    @Transactional
    public void delete(ModuleModel moduleModel) {
        List<LessonModel> lessons = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());
        if(!lessons.isEmpty()) {
            lessonRepository.deleteAll(lessons);
        }
        repository.delete(moduleModel);
    }

    @Override
    public ModuleModel save(ModuleModel moduleModel) {
        return repository.save(moduleModel);
    }

    @Override
    public Optional<ModuleModel> findById(UUID moduleId) {
        return repository.findById(moduleId);
    }

    @Override
    public List<ModuleModel> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
        return repository.findModuleIntoCourse(courseId, moduleId);
    }

    @Override
    public List<ModuleModel> findAllByCourse(UUID courseId) {
        return repository.findAllModulesIntoCourse(courseId);
    }

    @Override
    public Page<ModuleModel> findAllByCourse(Specification<ModuleModel> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }
}
