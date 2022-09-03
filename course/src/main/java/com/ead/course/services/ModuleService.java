package com.ead.course.services;

import com.ead.course.model.ModuleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {

    void delete(ModuleModel moduleModel);

    ModuleModel save(ModuleModel moduleModel);

    Optional<ModuleModel> findById(UUID moduleId);

    List<ModuleModel> findAll();

    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);

    List<ModuleModel> findAllByCourse(UUID courseId);
}
