package de.hhu.abschlussprojektverleihplattform.TestDummys;

import de.hhu.abschlussprojektverleihplattform.logic.IUser;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.ArrayList;

public class DummyUserService implements IUser {

    private ArrayList<UserEntity> users;

    public DummyUserService() {
        users = new ArrayList<>();
    }

    @Override
    public void addUser(UserEntity user) {
        users.add(user);
    }

    public ArrayList<UserEntity> getUsers() {
        return users;
    }
}
