package ro.pyc22.shop.repositories;

import ro.pyc22.shop.model.User;

import java.util.Optional;

public interface UserRepository <T extends User> {

    T create (T t);

    T getUserByEmail(String email);
}
