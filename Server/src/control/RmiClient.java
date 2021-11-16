package control;

import dto.AddFriendDTO;
import dto.RequestDTO;
import dto.SendMessageDTO;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.IPAddress;
import model.User;
import impl.AuthInterface;
import impl.ChatInterface;
import impl.FriendInterface;
import impl.UserInterface;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Conversation;
import model.Message;
import view.ServerMainFrm;

public class RmiClient {

    private ServerMainFrm view;
    private AuthInterface authRo;
    private UserInterface userRo;
    private FriendInterface friendRo;
    private ChatInterface chatRo;

    private IPAddress serverAddress = new IPAddress("localhost", 7611);
    private String rmiService = "rmiServer";

    public RmiClient(ServerMainFrm view) {
        this.view = view;
    }

    public RmiClient(ServerMainFrm view, String serverHost, int serverPort, String service) {
        this.view = view;
        serverAddress.setHost(serverHost);
        serverAddress.setPort(serverPort);
        rmiService = service;
    }

    public void init() {
        try {
            // get the registry
            Registry registry = LocateRegistry.getRegistry(serverAddress.getHost(), serverAddress.getPort());
            // lookup the remote objects
            authRo = (AuthInterface) (registry.lookup(rmiService));
            userRo = (UserInterface) (registry.lookup(rmiService));
            friendRo = (FriendInterface) (registry.lookup(rmiService));
            chatRo = (ChatInterface) (registry.lookup(rmiService));

            view.showMessage("Found the remote objects at the host: " + serverAddress.getHost() + ", port: " + serverAddress.getPort());
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Error to lookup the remote objects!");
            JOptionPane.showMessageDialog(view, "RMI server has not started !");
        }
    }

    public User remoteLogin(User user) {
        try {
            return authRo.login(user);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean remoteSignup(User user) {
        try {
            return authRo.signup(user);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<AddFriendDTO> remoteGetUsers(Long myId, String username) {
        try {
            return userRo.getUsers(myId, username);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<User> remoteGetFriends(Long myId) {
        try {
            return friendRo.getFriends(myId);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Long remoteAddFriend(ArrayList<Long> ids) {
        try {
            return friendRo.addFriend(ids);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<RequestDTO> remoteGetRequests(Long id) {
        try {
            return friendRo.getRequests(id);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Long remoteConfirmFriend(RequestDTO requestDTO) {
        try {
            return friendRo.confirmFriend(requestDTO);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Long remoteDeclineFriend(Long friendId, Long toId) {
        try {
            return friendRo.declineFriend(friendId, toId);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // chat
    public ArrayList<Conversation> remoteGetListConverstation(Long id) {
        System.out.println("REMOTE_GETLIST 1: " + id);
        try {
            return chatRo.getConverstation(id);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean remoteCreateConverstation(ArrayList<User> users) {
        try {
            return chatRo.createConverstation(users);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<Message> remoteGetListMessage(Long id) {
        try {
            return chatRo.getMessageConverstation(id);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean remoteTriggerStatus(Long id, int status) {
        try {
            authRo.triggerStatus(id, status);
            return true;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean remoteSendMessage(SendMessageDTO mess) {
        try {
            return chatRo.sendMessage(mess);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<User> remoteGetAllUsersInConversation(Long id) {
        try {
            return chatRo.getUserConverstation(id);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
