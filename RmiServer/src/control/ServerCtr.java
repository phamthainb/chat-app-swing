/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import dao.UserDao;
import impl.UserInterface;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import model.IPAddress;
import model.User;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import view.ServerMainFrm;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import impl.FriendInterface;
import dao.FriendDao;
import dto.AddFriendDTO;
import dto.RequestDTO;
import dto.SendMessageDTO;
import impl.ChatInterface;
import model.Conversation;
import model.Message;
import dao.ConversartionDAO;
import dao.MessageDAO;
import model.Friend;

/**
 *
 * @author son
 */
public class ServerCtr extends UnicastRemoteObject implements UserInterface, FriendInterface, ChatInterface {

    private IPAddress myAddress = new IPAddress("localhost", 7611);
    private Registry registry;
    private ServerMainFrm view;
    private String rmiService = "rmiServer";

    private ConversartionDAO cdao = new ConversartionDAO();
    private MessageDAO mdao = new MessageDAO();

    private UserDao userDao = new UserDao();
    private FriendDao friendDao = new FriendDao();

    public ServerCtr(ServerMainFrm view) throws RemoteException {
        this.view = view;
    }

    public ServerCtr(ServerMainFrm view, int port, String service) throws RemoteException {
        this.view = view;
        myAddress.setPort(port);
        this.rmiService = service;
    }

    public void start() throws RemoteException {
        // registry this to the localhost
        try {
            try {
                //create new one
                registry = LocateRegistry.createRegistry(myAddress.getPort());
            } catch (ExportException e) {
                registry = LocateRegistry.getRegistry(myAddress.getPort());
            }
            registry.rebind(rmiService, this);
            myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
            view.showMessage("The RIM has registered the service key: " + rmiService + ", at the port: " + myAddress.getPort());
        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() throws RemoteException {
        // unbind the service
        try {
            if (registry != null) {
                registry.unbind(rmiService);
                UnicastRemoteObject.unexportObject(this, true);
            }
            view.showMessage("The RIM has unbinded the service key: " + rmiService + ", at the port: " + myAddress.getPort());
        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User login(User user) throws RemoteException {
        return userDao.login(user);
    }

    @Override
    public boolean signup(User user) throws RemoteException {
        return userDao.signup(user);
    }

    @Override
    public ArrayList<User> getUsers(User user) throws RemoteException {
        return userDao.getUsers(user);
    }

    @Override
    public ArrayList<Friend> getFriends(Long myId) throws RemoteException {
        return friendDao.getFriends(myId);
    }

    @Override
    public Long addFriend(Friend friend) throws RemoteException {
        return friendDao.addFriend(friend);
    }

    @Override
    public ArrayList<Friend> getRequests(Long id) throws RemoteException {
        return friendDao.getRequests(id);
    }

    @Override
    public Long confirmFriend(Friend friend) throws RemoteException {
        return friendDao.confirmFriend(friend);
    }

    @Override
    public Long declineFriend(Friend fr) throws RemoteException {
        return friendDao.declineFriend(fr);
    }

    @Override
    public void triggerStatus(User user) throws RemoteException {
        userDao.triggerStatus(user);
    }

    @Override
    public ArrayList<Conversation> getConverstation(Long id) throws RemoteException {
        return cdao.getAllConverstation(id);
    }

    @Override
    public ArrayList<User> getUserConverstation(Long c_id) throws RemoteException {
        return cdao.getAllUserInConverstation(c_id);
    }

    @Override
    public boolean createConverstation(ArrayList<User> list) throws RemoteException {
        return cdao.createConverstation(list);
    }

    @Override
    public ArrayList<Message> getMessageConverstation(Long converstation_id) throws RemoteException {
        return mdao.getMessageConverstation(converstation_id);
    }

    @Override
    public boolean sendMessage(SendMessageDTO sendMessageDTO) throws RemoteException {
        return mdao.sendMessage(sendMessageDTO);
    }

    @Override
    public Friend getFriend(Friend friend) throws RemoteException {
        return friendDao.getFriend(friend);
    }

    @Override
    public Long cancelFriend(Friend friend) throws RemoteException {
        return friendDao.cancelFriend(friend);
    }

    @Override
    public Long deleteFriend(Friend friend) throws RemoteException {
        return friendDao.cancelFriend(friend);
    }
}
