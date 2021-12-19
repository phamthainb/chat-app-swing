/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import model.Conversation;
import model.ConversationUser;
import model.User;

/**
 *
 * @author phamthainb
 */
public class ConversartionDAO extends DAO {

    public boolean createConverstation(List<User> list) {
        String name = "";
        for (int i = 0; i < list.size() - 1; i++) {
            name += list.get(i).getUsername() + ", ";
        }
        name += list.get(list.size() - 1).getUsername();

        try {
            session.beginTransaction();
            session.clear();
            Conversation newC = new Conversation();
            newC.setName(name);
            session.saveOrUpdate(newC);
            //
            for (User u : list) {
                session.clear();
                ConversationUser cu = new ConversationUser();
                cu.setConversation(newC);
                cu.setUser(u);
                session.saveOrUpdate(cu);
            }

            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<User> getAllUserInConverstation(Long cid) {
        ArrayList<User> res = new ArrayList<>();
        try {
            ArrayList<ConversationUser> resC = (ArrayList<ConversationUser>) session.createQuery("select c from ConversationUser c WHERE c.conversation.id = :cid")
                    .setParameter("cid", cid)
                    .list();
            System.out.println("resC "+ resC.size());
            for (ConversationUser conversationUser : resC) {
                res.add(conversationUser.getUser());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // get all converstation of an user
    public List<Conversation> getAllConverstation(Long user_id) {
        List<Conversation> res = new ArrayList<>();
        try {
            res = session.createQuery("select c.conversation from ConversationUser c WHERE c.user.id = :uid")
                    .setParameter("uid", user_id)
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
