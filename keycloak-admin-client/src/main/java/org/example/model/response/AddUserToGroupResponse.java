package org.example.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.model.Group;
import org.example.model.User;

@Data
@AllArgsConstructor
public class AddUserToGroupResponse {
    private User user;
    private Group group;

//    public AddUserToGroupResponse(UserRepresentation user, GroupRepresentation group) {
//        this.user = user;
//        this.group = group;
//    }
}
