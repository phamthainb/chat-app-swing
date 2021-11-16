package dto;

import java.io.Serializable;

/**
 *
 * @author sonht
 */
public class AddFriendDTO implements Serializable {
    private Long userId;
    private Long fromId;
    private Long toId;
    private String username;
    private int confirmed;
    private Long friendId;

    public AddFriendDTO() {
    }

    public AddFriendDTO(Long userId, Long fromId, Long toId, String username, int confirmed, Long friendId) {
        this.userId = userId;
        this.fromId = fromId;
        this.toId = toId;
        this.username = username;
        this.confirmed = confirmed;
        this.friendId = friendId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
    
    
}
