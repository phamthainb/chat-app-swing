/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.Conversation;
import model.User;

public class ConversartionDAO extends DAO {

    public boolean createConverstation(ArrayList<User> list) {
        boolean result = true;

        String name = "";
        for (int i = 0; i < list.size() - 1; i++) {
            name += list.get(i).getUsername() + ", ";
        }
        name += list.get(list.size() - 1).getUsername();

        // create 
        String sql1 = "INSERT INTO conversation (name) values (?);";
        String sql2 = "INSERT into conversation_user (conversation_id, user_id) values (?, ?);";

        try {
            PreparedStatement ps = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.executeUpdate();

            ResultSet getGeneratedKeys = ps.getGeneratedKeys();
            if (getGeneratedKeys.next()) {
                Long cid = getGeneratedKeys.getLong(1);
                System.out.println("cid >>" + cid);

                for (User user : list) {
                    ps = con.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
                    ps.setLong(1, cid);
                    ps.setLong(2, user.getId());

                    int rs = ps.executeUpdate();
                    ResultSet getGeneratedKeys1 = ps.getGeneratedKeys();
                    if (!getGeneratedKeys1.next()) {//unavailable
                        result = false;
                        try {
                            con.rollback();
                            con.setAutoCommit(true);
                        } catch (Exception ex) {
                            result = false;
                            ex.printStackTrace();
                        }
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    public ArrayList<User> getAllUserInConverstation(Long cid) {
        ArrayList<User> res = new ArrayList<>();
        String sql = "	SELECT\n"
                + "	u.*\n"
                + "	from\n"
                + "		conversation_user cu ,\n"
                + "		user u\n"
                + "	WHERE\n"
                + "		cu.conversation_id = ?\n"
                + "		and cu.user_id = u.id;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cid + "");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User c = new User();
                c.setId(rs.getLong("id"));
                c.setUsername(rs.getString("username"));
                c.setOnline(rs.getInt("online"));
                res.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // get all converstation of an user
    public ArrayList<Conversation> getAllConverstation(Long user_id) {
        ArrayList<Conversation> res = new ArrayList<>();
        String sql = "SELECT c.id , c.name from conversation_user cu, conversation c WHERE cu.user_id = ? and cu.conversation_id = c.id;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, String.valueOf(user_id));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Conversation c = new Conversation();
                c.setId(rs.getLong("id"));
                c.setName(rs.getString("name"));
                // get list User
                ArrayList<User> listUser = getAllUserInConverstation(c.getId());
                c.setUsers(listUser);
                res.add(c);
                System.out.println(res.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
