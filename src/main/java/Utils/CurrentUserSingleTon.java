package Utils;

import Domain.UserDTO;

public class CurrentUserSingleTon {
    UserDTO user;

    private final static CurrentUserSingleTon currentUser = new CurrentUserSingleTon();

    private CurrentUserSingleTon() {

    }

    public static CurrentUserSingleTon getInstance() {
        return currentUser;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO new_user) {
        user = new_user;
    }
}
