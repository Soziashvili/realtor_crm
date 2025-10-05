package ru.realtorcrm.core_crm.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.realtorcrm.core_crm.dto.UserDto;
import ru.realtorcrm.core_crm.entity.User;
import ru.realtorcrm.core_crm.exception.UserAlreadyExistException;
import ru.realtorcrm.core_crm.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(UserDto userDto) {
        if (userRepository.isNicknameExists(userDto.getNickname())) {
            throw new UserAlreadyExistException("nickname '" + userDto.getNickname() + "'");
        }
        if (userRepository.isEmailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException("email '" + userDto.getEmail() + "'");
        }

        User user = User.createUserFromDto(userDto);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with id %s is not exist", id)
                ));
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(UserDto userDto, Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setNickname(userDto.getNickname());
                    user.setName(userDto.getName());
                    user.setPhoneNumber(userDto.getPhoneNumber());
                    user.setEmail(userDto.getEmail());
                    return userRepository.save(user);
                })
                .orElseGet(() -> userRepository.save(User.createUserFromDto(userDto)));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }
}
