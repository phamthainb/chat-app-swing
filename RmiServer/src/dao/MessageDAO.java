/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.SendMessageDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Conversation;
import model.Message;
import model.User;

/**
 *
 * @author phamthainb
 */
public class MessageDAO extends DAO {

    public ArrayList<Message> getMessageConverstation(Long converstation_id) {
        ArrayList<Message> res = new ArrayList<>();
        try {
            res = (ArrayList<Message>) session.createQuery("select m from Message m WHERE m.conversation.id = :cid")
                    .setParameter("cid", converstation_id)
                    .list();
        } catch (Exception e) {
        }
        return res;
    }

    public boolean sendMessage(Message m) {
        try {
            session.beginTransaction();
            session.clear();
            session.save(m);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
