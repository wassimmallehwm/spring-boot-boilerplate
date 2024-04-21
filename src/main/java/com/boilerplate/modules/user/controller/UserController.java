package com.boilerplate.modules.user.controller;

import com.boilerplate.configuration.exceptions.DataNotFoundException;
import com.boilerplate.configuration.exceptions.DuplicatedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.boilerplate.utils.datagrid.domain.GridModel;
import com.boilerplate.modules.user.dto.*;
import com.boilerplate.modules.user.service.IUserService;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    @Autowired
    private IUserService userService;

    @GetMapping(value = "/page")
    public ResponseEntity<?> getPage(GridModel params) {
        Page<UserDto> response = userService.getPage(params);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping(value = "/")
    public ResponseEntity<?> create(
            @RequestBody UserDto user
    ) throws DuplicatedException {
        UserDto response = userService.create(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody UserDto user
    ) throws DataNotFoundException, DuplicatedException {
        UserDto response = userService.update(id, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) throws DataNotFoundException {
        UserDto response = userService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) throws DataNotFoundException {
        userService.remove(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
