/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Conversation;
import model.User;

/**
 *
 * @author sonht
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypingDTO implements Serializable {

    private User user;
    private Conversation conversation;
    private boolean typing;
}
