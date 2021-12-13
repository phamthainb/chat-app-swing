/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.AddFriendDTO;
import model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author sonht
 */
public class UserDao extends DAO {

    public User login(User user) {
        try {
            Query query = session.createQuery("from User u where u.username = :username and u.password = :pwd");
            query.setParameter("username", user.getUsername());
            query.setParameter("pwd", user.getPassword());
            List<User> users = query.getResultList();
            return users.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean signup(User user) {
        boolean result = true;
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<User> getUsers(User user) {
        try {
            Query query = session.createQuery("from User u where u.id != :id and username like :keyword");
            query.setParameter("id", user.getId());
            query.setParameter("keyword", '%' + user.getUsername() + '%');
            List<User> users = query.getResultList();
            return (ArrayList<User>) users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean triggerStatus(User user) {
        try {
            session.beginTransaction();
            session.createQuery("update User u set u.online = :online where u.id = :id")
                    .setParameter("online", user.getOnline())
                    .setParameter("id", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
