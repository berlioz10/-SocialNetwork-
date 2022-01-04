package Utils;

import java.util.Objects;

public class Hasher {
    public static String hash(String pass) {
        return pass;
    }

    public static boolean isHashedCorrectly(String hash, String pass) {
        return Objects.equals(hash, hash(pass));

    }
}
