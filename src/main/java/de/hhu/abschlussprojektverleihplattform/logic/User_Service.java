package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

public interface User_Service {
    void addUser(UserEntity user);

    // die user werden in der logik nur erstellt, nicht veraendert. das finden des jeweiligen richtigen users muss in den controllern geschehen.
}
