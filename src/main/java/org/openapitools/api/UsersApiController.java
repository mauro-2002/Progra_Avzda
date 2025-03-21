package org.openapitools.api;

import org.openapitools.model.*;


import org.openapitools.services.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-03-04T19:57:39.346325400-05:00[America/Bogota]", comments = "Generator version: 7.7.0")
@RestController
@RequestMapping("/users")
public class UsersApiController implements UsersApi {

    private final NativeWebRequest request;
    private final UserService userService;

    @Autowired
    public UsersApiController(NativeWebRequest request, UserService userService) {
        this.request = request;
        this.userService = userService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }


    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser (@RequestBody @Valid UserRegistration userRegistration){
        return ResponseEntity.ok(userService.createUser(userRegistration));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers (){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById (@PathVariable("id") @Valid String id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable("id") @RequestBody @Valid User user){
        return ResponseEntity.ok(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/users/{id}/password")
    public ResponseEntity deleteUser (@PathVariable("id") String id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

}

