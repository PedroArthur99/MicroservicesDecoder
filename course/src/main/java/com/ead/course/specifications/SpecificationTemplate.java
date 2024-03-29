package com.ead.course.specifications;

import com.ead.course.model.CourseModel;
import com.ead.course.model.CourseUserModel;
import com.ead.course.model.LessonModel;
import com.ead.course.model.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface LessonSpec extends Specification<LessonModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<ModuleModel> {}

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, cb) -> {
          query.distinct(true);
          Root<ModuleModel> module = root;
          Root<CourseModel> course = query.from(CourseModel.class);
          Expression<Collection<ModuleModel>> coursesModules = course.get("modules");
          return cb.and(cb.equal(course.get("id"), courseId), cb.isMember(module, coursesModules));
        };
    }

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<LessonModel> lesson = root;
            Root<ModuleModel> module = query.from(ModuleModel.class);
            Expression<Collection<LessonModel>> modulesLessons = module.get("lessons");
            return cb.and(cb.equal(lesson.get("lessonID"), moduleId), cb.isMember(lesson, modulesLessons));
        };
    }

    public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<CourseModel, CourseUserModel> courseProd = root.join("courseUsers");
            return criteriaBuilder.equal(courseProd.get("userId"), userId);
        };
    }

}
