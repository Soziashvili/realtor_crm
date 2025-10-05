package ru.realtorcrm.core_crm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.realtorcrm.core_crm.dto.UserDto;
import ru.realtorcrm.core_crm.entity.User;
import ru.realtorcrm.core_crm.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.createUser(userDto);
            log.info("User with id {} was added", user.getId() );
            return ResponseEntity.ok(user);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                           @RequestBody UserDto userDto) {
        try {
            User user = userService.updateUser(userDto, id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
