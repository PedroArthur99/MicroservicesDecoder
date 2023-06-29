package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorDTO;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/instructors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InstructorController {

    @Autowired
    UserService userService;

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDTO instructorDTO) {

        Optional<UserModel> userModel = userService.findById(instructorDTO.getUserId());
        if (userModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found...");
        }
        else {
            UserModel model = userModel.get();
            model.setUserType(UserType.INSTRUCTOR);
            model.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(model);
            return ResponseEntity.status(HttpStatus.OK).body(model);
        }

    }
}
