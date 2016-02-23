package com.vacoola.backend.service;

import com.vacoola.backend.entity.User;

import java.util.List;

/**
 * Created by r.vakulenko on 20.02.2016.
 */
public interface UserService {

    public void save(User user);

    public void delete(User user);

    public List<User> getPageUsers(String filter, Paging paging);

    public List<User> getAllUsers();

}
