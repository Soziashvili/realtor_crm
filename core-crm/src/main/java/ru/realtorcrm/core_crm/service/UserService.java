package ru.realtorcrm.core_crm.service;

import ru.realtorcrm.core_crm.dto.BuildingRequestDto;
import ru.realtorcrm.core_crm.dto.UserDto;
import ru.realtorcrm.core_crm.entity.Building;
import ru.realtorcrm.core_crm.entity.User;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(UserDto userDto, Long id);
    void deleteUser(Long id);
}
