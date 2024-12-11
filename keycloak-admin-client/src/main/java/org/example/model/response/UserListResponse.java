package org.example.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.model.Group;
import org.example.model.User;

import java.util.List;

@Data
@AllArgsConstructor
public class UserListResponse {
    private Group group;
    private List<User> userList;
}
