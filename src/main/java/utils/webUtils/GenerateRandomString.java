package utils.webUtils;
import java.security.SecureRandom;
import java.util.stream.Collectors;
public class GenerateRandomString {
    public static String generateRandomString() {

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();

        String randomString = random.ints(6, 0, alphabet.length())
                .mapToObj(alphabet::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
        return randomString;
    }
}
