package com.ead.course.controllers;

import com.ead.course.controllers.dtos.CourseDTO;
import com.ead.course.model.CourseModel;
import com.ead.course.services.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDTO courseDTO) {
        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "id") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        else {
            courseService.delete(courseModelOptional.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted succesfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "id") UUID courseId,
                                               @RequestBody @Valid CourseDTO courseDTO) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        CourseModel courseModel = courseModelOptional.get();
        courseModel.setName(courseDTO.getName());
        courseModel.setDescription(courseDTO.getDescription());
        courseModel.setImageUrl(courseDTO.getImageUrl());
        courseModel.setCourseStatus(courseDTO.getCourseStatus());
        courseModel.setCourseLevel(courseDTO.getCourseLevel());
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }

    @GetMapping
    public ResponseEntity<List<CourseModel>> getAllCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "id") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional.get());
    }
}
