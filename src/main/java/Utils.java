public class Utils {
    public static int getDigitLeft(long number, int pos) {
        return (int) String.valueOf(number).charAt(pos) - '0'; // Default value if digit position is out of range
    }
}
