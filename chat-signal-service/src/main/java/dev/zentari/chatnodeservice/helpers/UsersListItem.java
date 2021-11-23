package dev.zentari.chatnodeservice.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UsersListItem implements Serializable {

    private String username;
    private UserConnectDisconnect action;
}
