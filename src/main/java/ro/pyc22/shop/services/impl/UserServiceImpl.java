package ro.pyc22.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.pyc22.shop.model.User;
import ro.pyc22.shop.model.modelDTO.UserDTO;
import ro.pyc22.shop.model.modelDTO.UserDTOMapper;
import ro.pyc22.shop.repositories.UserRepository;
import ro.pyc22.shop.services.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService<User> {

    private final UserRepository<User> userRepository;
    @Override
    public User create(User user) {

     return  userRepository.create(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {

           return  UserDTOMapper.formUser(userRepository.getUserByEmail(email));


    }
}
