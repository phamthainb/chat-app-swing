/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.SendMessageDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
        String sql
                = "  SELECT\n"
                + "    m.id, m.content,\n"
                + "    u.id as uid,\n"
                + "    u.username as uusername ,\n"
                + "    c.id as cid,\n"
                + "    c.name as cname\n"
                + "  FROM\n"
                + "    conversation c ,\n"
                + "    message m,\n"
                + "    user u \n"
                + "  WHERE\n"
                + "    c.id = ?\n"
                + "    and m.conversation_id = c.id\n"
                + "    and u.id = m.user_id;\n";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, converstation_id + "");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message c = new Message();
                c.setId(rs.getLong("id"));
                c.setContent(rs.getString("content"));

                User u = new User();
                u.setId(rs.getLong("uid"));
                u.setUsername(rs.getString("uusername"));
                c.setUser(u);

                Conversation cs = new Conversation();
                cs.setId(rs.getLong("cid"));
                cs.setName(rs.getString("cname"));
                c.setConversation(cs);

                res.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean sendMessage(SendMessageDTO sendMessageDTO) {
        String sql
                = "INSERT into message (user_id, conversation_id, content) values (?, ?,? );";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sendMessageDTO.getUser_id() + "");
            ps.setString(2, sendMessageDTO.getConverstation_id() + "");
            ps.setString(3, sendMessageDTO.getContent() + "");

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
