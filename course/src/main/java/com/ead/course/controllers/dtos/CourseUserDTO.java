package com.ead.course.controllers.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CourseUserDTO {

    private UUID userId;
    private UUID courseId;

}
