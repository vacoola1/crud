package com.vacoola.backend.dao;

import com.vacoola.backend.entity.User;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class UserDaoImpl implements UserDao {
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(User user) {
        if (user.getAdmin()==null) {
            user.setAdmin(false);
        }
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public void update(User user) {
        if (user.getAdmin()==null) {
            user.setAdmin(false);
        }
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    public void delete(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }

    /*NOP*/
    @Override
    public List<User> getPageUsers(long from, long pageSize) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User");
        query.setFirstResult((int)from);
        query.setMaxResults((int)pageSize);
        return (List<User>)query.list();
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) sessionFactory.getCurrentSession().createQuery("from User").list();
    }


    @Override
    public long countUsers() {
        List<Object> list = sessionFactory.getCurrentSession().createQuery("select COUNT(*) from User").list();
        return (long) list.get(0);
    }
}

