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

/**
 *
 * @author sonht
 */
public class UserDao extends DAO {

    public User login(User user) {
        User res = null;
        String sql = "SELECT DISTINCT id, username FROM user WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                res = new User();
                res.setId(rs.getLong("id"));
                res.setUsername(rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean signup(User user) {
        boolean result = true;
        String sql = "INSERT INTO user (username, password) VALUES(?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public ArrayList<AddFriendDTO> getUsers(Long myId, String keyword) {
        ArrayList<AddFriendDTO> users = new ArrayList<>();
//        String sql = "select DISTINCT user.username, user.id, friend.id as friendId, friend.user_id_1 as fromId, friend.user_id_2 as toId, confirmed from user LEFT JOIN friend ON (user.id = friend.user_id_1 OR user.id = friend.user_id_2) WHERE user.username like ? AND NOT user.id = ? AND ((friend.user_id_1 = ? OR friend.user_id_2 = ?) OR (friend.user_id_1 IS NULL and friend.user_id_2 IS NULL))";
        String sql = "SELECT DISTINCT\n"
                + "    user.username,\n"
                + "    user.id,\n"
                + "    friend.id AS friendId,\n"
                + "    friend.user_id_1 AS fromId,\n"
                + "    friend.user_id_2 AS toId,\n"
                + "    confirmed\n"
                + "FROM\n"
                + "    user\n"
                + "        LEFT JOIN\n"
                + "    (SELECT DISTINCT\n"
                + "        friend.id,\n"
                + "            friend.user_id_1,\n"
                + "            friend.user_id_2,\n"
                + "            friend.confirmed\n"
                + "    FROM\n"
                + "        user, friend\n"
                + "    WHERE\n"
                + "        (friend.user_id_1 = user.id\n"
                + "            OR friend.user_id_2 = user.id)\n"
                + "            AND user.id = ?) AS friend ON (user.id = friend.user_id_1\n"
                + "        OR user.id = friend.user_id_2)\n"
                + "WHERE\n"
                + "   user.username like ? and NOT user.id = ?;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, myId);
            ps.setString(2, '%' + keyword + '%');
            ps.setLong(3, myId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long userId = rs.getLong("id");
                Long fromId = rs.getLong("fromId");
                Long toId = rs.getLong("toId");
                Long friendId = rs.getLong("friendId");
                String username = rs.getString("username");
                int confirmed = rs.getInt("confirmed");

                users.add(new AddFriendDTO(userId, fromId, toId, username, confirmed, friendId));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    public void triggerStatus(Long id, int status) {
        String sql = "update user set online=? where id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
