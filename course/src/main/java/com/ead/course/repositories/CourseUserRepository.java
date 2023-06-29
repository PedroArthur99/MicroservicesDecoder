package com.ead.course.repositories;

import com.ead.course.model.CourseModel;
import com.ead.course.model.CourseUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {

    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

    @Query(value = "select * from tb_courses_users where course_id = :courseId", nativeQuery = true)
    List<CourseUserModel> findAllCourseUserIntoCourse(@Param("courseId") UUID courseId);
}
