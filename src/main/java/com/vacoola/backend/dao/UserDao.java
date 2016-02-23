package com.vacoola.backend.dao;


import com.vacoola.backend.entity.User;

import java.util.List;

/**
 * Created by r.vakulenko on 20.02.2016.
 */
public interface UserDao {

    public void save(User user);

    public void update(User user);

    public void delete(User user);

    public List<User> getPageUsers(long from, long pageSize);

    public List<User> getAllUsers();

    public long countUsers();

}
