/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import dao.UserDao;
import impl.AuthInterface;
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

/**
 *
 * @author son
 */
public class ServerCtr extends UnicastRemoteObject implements AuthInterface, UserInterface, FriendInterface, ChatInterface {

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
    public ArrayList<AddFriendDTO> getUsers(Long myId, String username) throws RemoteException {
        return userDao.getUsers(myId, username);
    }

    @Override
    public ArrayList<User> getFriends(Long myId) throws RemoteException {
        return friendDao.getFriends(myId);
    }

    @Override
    public Long addFriend(ArrayList<Long> ids) throws RemoteException {
        return friendDao.addFriend(ids);
    }

    @Override
    public ArrayList<RequestDTO> getRequests(Long id) throws RemoteException {
        return friendDao.getRequests(id);
    }

    @Override
    public Long confirmFriend(RequestDTO requestDTO) throws RemoteException {
        return friendDao.confirmFriend(requestDTO);
    }

    @Override
    public Long declineFriend(Long friendId, Long toId) throws RemoteException {
        return friendDao.declineFriend(friendId, toId);
    }

    @Override
    public void triggerStatus(Long id, int status) throws RemoteException {
        userDao.triggerStatus(id, status);
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
}