/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author phamthainb
 */
public class SendMessageDTO implements Serializable{

    private Long user_id;
    private Long converstation_id;
    private String content;

    public SendMessageDTO() {
    }

    public SendMessageDTO(Long user_id, Long converstation_id, String content) {
        this.user_id = user_id;
        this.converstation_id = converstation_id;
        this.content = content;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getConverstation_id() {
        return converstation_id;
    }

    public void setConverstation_id(Long converstation_id) {
        this.converstation_id = converstation_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
