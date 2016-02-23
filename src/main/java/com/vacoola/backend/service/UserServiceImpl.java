package com.vacoola.backend.service;

import com.vacoola.backend.dao.UserDao;
import com.vacoola.backend.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save(User user) {
        if (user == null) {
            return;
        } else if (user.getId() == 0) {
            user.setCreatedDate(new Timestamp(new Date().getTime()));
            userDao.save(user);
        } else {
            userDao.update(user);
        }
    }

    public void delete(User user) {

        if (user == null) {
            return;}
        userDao.delete(user);
    }

    public List<User> getPageUsers(String filter, Paging paging) {

        paging.updatePaging(countUsers(filter));

        List<User> userList = getUsers(filter, (paging.currentPage - 1) * paging.PAGE_SIZE, paging.PAGE_SIZE);
        if (userList.size() == 0) {
            paging.updatePaging(countUsers(filter));
            userList = getUsers(filter, (paging.maxPage - 1) * paging.PAGE_SIZE, paging.PAGE_SIZE);
        }

        return userList;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    private List<User> getUsers(String filter, long from, long pageSize) {
        if (filter == null || filter.isEmpty()) {
            return userDao.getPageUsers(from, pageSize);
        } else {
            List<User> listAll = userDao.getAllUsers();
            List<User> listFound = new ArrayList<>();
            for (User user : listAll) {
                if (user.toString().toLowerCase().contains(filter.toLowerCase())) {
                    listFound.add(user);
                }
            }

            List<User> listFromTo = new ArrayList<>();

            for (int i = 0; i < listFound.size(); i++) {
                if (i >= from && i < (from + pageSize)) {
                    listFromTo.add(listFound.get(i));
                }
            }
            return listFromTo;
        }
    }

    private long countUsers(String filter) {
        if (filter == null || filter.isEmpty()) {
            return userDao.countUsers();
        } else {
            List<User> listAll = userDao.getAllUsers();
            List<User> list = new ArrayList<>();
            for (User user : listAll) {
                if (user.toString().toLowerCase().contains(filter.toLowerCase())) {
                    list.add(user);
                }
            }

            return (long)list.size();
        }

    }
}