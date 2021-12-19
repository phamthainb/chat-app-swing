/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.RequestDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;
import model.Friend;

/**
 *
 * @author sonht
 */
public class FriendDao extends DAO {

    public FriendDao() {
    }

    public Friend getFriend(Friend friend) {
        try {
            User user1;
            User user2;
            Friend resFr;
            session.clear();
            user1 = (User) session.createQuery("from User u where u.id = :id")
                    .setParameter("id", friend.getUser_1().getId()).uniqueResult();
            user2 = (User) session.createQuery("from User u where u.id = :id")
                    .setParameter("id", friend.getUser_2().getId()).uniqueResult();
            resFr = (Friend) session.createNativeQuery("select * from friend where (friend.id_user_1 = :id1 and friend.id_user_2 = :id2) "
                    + "or (friend.id_user_1 = :id2 and friend.id_user_2 = :id1)", Friend.class)
                    .setParameter("id1", friend.getUser_1().getId())
                    .setParameter("id2", friend.getUser_2().getId())
                    .uniqueResult();
            if (resFr == null) {
                resFr = new Friend();
            }
            if (resFr.getUser_2() != null && resFr.getUser_2().getId().equals(user1.getId())) {
                resFr.setUser_1(user2);
                resFr.setUser_2(user1);
            } else {
                resFr.setUser_2(user2);
                resFr.setUser_1(user1);
            }
            return resFr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Friend> getFriends(Long id) {
        ArrayList<Friend> friends = new ArrayList<>();
        try {
            List<Object[]> res = session.createNativeQuery("SELECT \n"
                    + "    *\n"
                    + "FROM\n"
                    + "    user\n"
                    + "        INNER JOIN\n"
                    + "    (SELECT \n"
                    + "        friend.id AS friendId,\n"
                    + "            friend.id_user_1,\n"
                    + "            friend.id_user_2,\n"
                    + "            friend.confirmed\n"
                    + "    FROM\n"
                    + "        friend\n"
                    + "    WHERE\n"
                    + "        friend.id_user_1 = :id\n"
                    + "            OR friend.id_user_2 = :id) AS friend ON (user.id = friend.id_user_1\n"
                    + "        OR user.id = friend.id_user_2)\n"
                    + "WHERE\n"
                    + "    NOT user.id = :id AND friend.confirmed = 1;")
                    .setParameter("id", id)
                    .list();
            for (Object[] r : res) {
                User user2 = new User();
                Friend f = new Friend();

                int online = (int) r[3];
                String username = (String) r[5];
                boolean confirmed = (boolean) r[9];
                Long friendId = Long.parseLong(String.valueOf(r[6]));
                Long userId1 = Long.parseLong(String.valueOf(r[7]));
                Long userId2 = Long.parseLong(String.valueOf(r[8]));

                user2.setUsername(username);
                user2.setOnline(online);

                if (userId1.equals(id)) {
                    user2.setId(userId2);
                } else {
                    user2.setId(userId1);
                }
                f.setId(friendId);
                f.setConfirmed(confirmed);
                f.setUser_2(user2);

                friends.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return friends;
    }

    public Long addFriend(Friend friend) {
        try {
            session.beginTransaction();
            session.clear();
            session.save(friend);
            session.getTransaction().commit();
            return friend.getUser_2().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Friend> getRequests(Long id) {
        ArrayList<Friend> friends = new ArrayList<>();
        try {
            session.beginTransaction();
            session.clear();

            List<Object[]> res = session.createNativeQuery("SELECT \n"
                    + "    *\n"
                    + "FROM\n"
                    + "    user\n"
                    + "        INNER JOIN\n"
                    + "    (SELECT \n"
                    + "        friend.id AS friendId,\n"
                    + "            friend.id_user_1,\n"
                    + "            friend.id_user_2,\n"
                    + "            friend.confirmed\n"
                    + "    FROM\n"
                    + "        friend\n"
                    + "    WHERE\n"
                    + "        friend.id_user_2 = :id) AS friend ON (user.id = friend.id_user_1\n"
                    + "        OR user.id = friend.id_user_2)\n"
                    + "WHERE\n"
                    + "    NOT user.id = :id AND friend.confirmed = 0;")
                    .setParameter("id", id)
                    .getResultList();
            for (Object[] r : res) {
                User user1 = new User();
                Friend f = new Friend();

                int online = (int) r[3];
                String username = (String) r[5];
                boolean confirmed = (boolean) r[9];
                Long friendId = Long.parseLong(String.valueOf(r[6]));
                Long userId1 = Long.parseLong(String.valueOf(r[7]));
                Long userId2 = Long.parseLong(String.valueOf(r[8]));

                user1.setUsername(username);
                user1.setOnline(online);

                if (userId1.equals(id)) {
                    user1.setId(userId2);
                } else {
                    user1.setId(userId1);
                }
                f.setId(friendId);
                f.setConfirmed(confirmed);
                f.setUser_1(user1);

                friends.add(f);
            }
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return friends;
    }

    public Long confirmFriend(Friend fr) {
        try {
            session.beginTransaction();
            session.createQuery("update Friend fr set fr.confirmed = true where fr.id = :id")
                    .setParameter("id", fr.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fr.getUser_1().getId();
    }

    public Long declineFriend(Friend fr) {
        try {
            session.beginTransaction();
            session.createQuery("delete from Friend f where f.id = :id")
                    .setParameter("id", fr.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fr.getUser_1().getId();
    }

    public Long cancelFriend(Friend fr) {
        try {
            session.beginTransaction();
            session.createQuery("delete from Friend f where f.id = :id")
                    .setParameter("id", fr.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fr.getUser_2().getId();
    }

    // chat 
    public ArrayList<Friend> getChatFriend(User u) {
        return (ArrayList<Friend>) session.createQuery("select f from Friend f where f.user_1.id = :k or f.user_2.id = :m")
                .setParameter("k", u.getId())
                .setParameter("m", u.getId())
                .list();
    }
}
