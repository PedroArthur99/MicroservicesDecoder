package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.model.CourseModel;
import com.ead.course.model.CourseUserModel;
import com.ead.course.model.LessonModel;
import com.ead.course.model.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
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
public class CouseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseUserRepository courseUserRepository;

    @Autowired
    private AuthUserClient authUserClient;

    @Override
    @Transactional
    public void delete(CourseModel courseModel) {
        boolean deleteCourseUserInAuthUser = false;
        List<ModuleModel> modules = moduleRepository.findAllModulesIntoCourse(courseModel.getId());
        if (!modules.isEmpty()) {
            for (ModuleModel module : modules) {
                List<LessonModel> lessons = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if (!lessons.isEmpty()) {
                    lessonRepository.deleteAll(lessons);
                }
            }
            moduleRepository.deleteAll(modules);
        }
        List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUserIntoCourse(courseModel.getId());
        if (!courseUserModelList.isEmpty()) {
            courseUserRepository.deleteAll(courseUserModelList);
            deleteCourseUserInAuthUser = true;
        }
        repository.delete(courseModel);

        if (deleteCourseUserInAuthUser) {
            authUserClient.deleteCourseInAuthUser(courseModel.getId());
        }
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return repository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return repository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }
}
