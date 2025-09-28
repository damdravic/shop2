package ro.pyc22.shop.services;

import ro.pyc22.shop.model.AuthenticatedUser;
import ro.pyc22.shop.model.User;
import ro.pyc22.shop.model.modelDTO.UserDTO;

public interface UserService<T extends User> {

    T create(T t);
    UserDTO getUserByEmail(String email);

    AuthenticatedUser getAuthenticatedUserByUserId (String email);

}
