package de.hhu.abschlussprojektverleihplattform.service.propay;

import org.apache.commons.lang3.RandomStringUtils;

public class ProPayUtils {
    public static String make_new_user() {
        return "user_account+"+RandomStringUtils.random(10, false, true);
    }
}
