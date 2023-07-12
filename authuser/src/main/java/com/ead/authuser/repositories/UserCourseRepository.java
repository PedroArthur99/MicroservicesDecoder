package com.ead.authuser.repositories;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID> {

    boolean existsByUserAndCourseId(UserModel userModel, UUID courseId);

    @Query(value = "select * from tb_users_courses where user_id = :user_id", nativeQuery = true)
    List<UserCourseModel> findAllUserCourseIntoUser(UUID user_id);

    boolean existsByCourseId(UUID courseId);

    void deleteAllByCourseId(UUID courseId);
}
