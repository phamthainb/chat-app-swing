/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import static dao.DAO.session;
import java.util.ArrayList;
import model.FileMessage;
import model.Message;

/**
 *
 * @author phamthainb
 */
public class FileDao extends DAO {

    public ArrayList<FileMessage> getFileMessage(Long converstation_id) {
        ArrayList<FileMessage> res = new ArrayList<>();
        try {
            res = (ArrayList<FileMessage>) session.createQuery("select m from FileMessage m WHERE m.conversation.id = :cid")
                    .setParameter("cid", converstation_id)
                    .list();
        } catch (Exception e) {
        }
        return res;
    }

    public boolean sendFileMessage(FileMessage m) {
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
