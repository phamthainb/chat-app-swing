package control;

import dto.AddFriendDTO;
import dto.RequestDTO;
import dto.SendMessageDTO;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.IPAddress;
import model.User;
import model.Friend;
import impl.ChatInterface;
import impl.FileMessageInterface;
import impl.FriendInterface;
import impl.UserInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Conversation;
import model.FileMessage;
import model.Message;
import view.ServerMainFrm;

public class RmiClient {

    private ServerMainFrm view;
    private UserInterface userRo;
    private FriendInterface friendRo;
    private ChatInterface chatRo;
    private FileMessageInterface fileMessageRo;

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
            userRo = (UserInterface) (registry.lookup(rmiService));
            friendRo = (FriendInterface) (registry.lookup(rmiService));
            chatRo = (ChatInterface) (registry.lookup(rmiService));
            fileMessageRo = (FileMessageInterface) (registry.lookup(rmiService));

            view.showMessage("Found the remote objects at the host: " + serverAddress.getHost() + ", port: " + serverAddress.getPort());
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Error to lookup the remote objects!");
            JOptionPane.showMessageDialog(view, "RMI server has not started !");
        }
    }

    public User remoteLogin(User user) {
        try {
            return userRo.login(user);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean remoteSignup(User user) {
        try {
            return userRo.signup(user);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<User> remoteGetUsers(User user) {
        try {
            return userRo.getUsers(user);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<Friend> remoteGetFriends(Long myId) {
        try {
            return friendRo.getFriends(myId);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Long remoteAddFriend(Friend friend) {
        try {
            return friendRo.addFriend(friend);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<Friend> remoteGetRequests(Long id) {
        try {
            return friendRo.getRequests(id);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Long remoteConfirmFriend(Friend friend) {
        try {
            return friendRo.confirmFriend(friend);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Long remoteDeclineFriend(Friend friend) {
        try {
            return friendRo.declineFriend(friend);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Long remoteCancelFriend(Friend friend) {
        try {
            return friendRo.cancelFriend(friend);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Long remoteDeleteFriend(Friend friend) {
        try {
            return friendRo.deleteFriend(friend);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // chat
    public List<Conversation> remoteGetListConverstation(Long id) {
        //System.out.println("REMOTE_GETLIST 1: " + id);
        try {
            return chatRo.getConverstation(id);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean remoteCreateConverstation(List<User> users) {
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

    public boolean remoteTriggerStatus(User user) {
        try {
            return userRo.triggerStatus(user);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean remoteSendMessage(Message mess) {
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

    public Friend remoteGetFriend(Friend friend) {
        try {
            return friendRo.getFriend(friend);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<Friend> remoteGetChatFriends(User u) {
        try {
            return friendRo.getChatFriend(u);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    // file 

    public Boolean remoteSaveFile(FileMessage f) {
        try {
            return fileMessageRo.saveFile(f);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<FileMessage> remoteGetListFile(Conversation c) {
        try {
            return fileMessageRo.getListFile(c);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
