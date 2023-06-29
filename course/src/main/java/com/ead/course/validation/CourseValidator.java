package com.ead.course.validation;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.controllers.dtos.CourseDTO;
import com.ead.course.controllers.dtos.UserDTO;
import com.ead.course.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    AuthUserClient authUserClient;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseDTO courseDTO = (CourseDTO) target;
        validator.validate(courseDTO, errors);
        if (!errors.hasErrors()) {
            validateUserInstructor(courseDTO.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        ResponseEntity<UserDTO> responseUserInstructor;
        try {
            responseUserInstructor = authUserClient.getOneUserById(userInstructor);
            if (responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)) {
                errors.rejectValue("userInstructor", "UserInstructor error", "User must be INSTRUCTOR or ADMIM.");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                errors.rejectValue("userInstructor", "UserInstructor error", "User not found.");
            }
        }

    }
}
