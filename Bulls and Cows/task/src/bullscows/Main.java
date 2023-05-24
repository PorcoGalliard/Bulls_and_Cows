package bullscows;

import java.util.*;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int NUMBER_LENGTH = 4;
    private static final StringBuilder SB = new StringBuilder();

    public static void main(String[] args) {
        try {
            playTheBullsAndCow();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }


    private static void playTheBullsAndCow() {
        System.out.println("Input the length of secret code:");
        int length = 0;

        try {
            length = Integer.parseInt(SCANNER.nextLine());
            if (length < 1) {
                throw new IllegalArgumentException("Error: the minimum number of the length is 1");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Error: \"%d\" isn't a valid number", length));
        }

        System.out.println("Input the number of possible symbols in the code:");
        int possibleCharacter = SCANNER.nextInt();
        SCANNER.nextLine();

        if (possibleCharacter < length) {
            throw new IllegalArgumentException(String.format("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", length, possibleCharacter));
        } else if (possibleCharacter > 36) {
            throw new IllegalArgumentException("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
        } else if (length < 1) {
            throw new IllegalArgumentException("Error: the minimum number of the length is 1");
        }

        int[] secretNumber = newGeneratedSecretNumber(length, possibleCharacter);

        SB.append("*".repeat(Math.max(0, length)));

        char lastCharacter;
        if (possibleCharacter <= 10) {
            lastCharacter = (char) ('0' + possibleCharacter - 1);
        } else {
            lastCharacter = (char) ('a' + possibleCharacter - 11);
        }

        String guessTheSecretNumber = SB.toString();
        System.out.printf("The secret is prepared: %s (0-9, a-%c).%n", guessTheSecretNumber, lastCharacter);

        System.out.println("Okay, let's start a game!");
        int turn = 1;

        while (true) {
            System.out.printf("%nTurn %d%n:", turn);
            int[] guess = newGetGuessedNumber();
            int countBulls = newCountBulls(guess, secretNumber, length);
            int countCows = newCountCows(guess, secretNumber, length);

            if (countBulls == length) {
                System.out.printf("Grade: %d bulls\n", countBulls);
                System.out.println("Congratulations! You guessed the secret code.");
                break;
            } else if (countBulls == 0 && countCows == 0){
                System.out.printf("Grade: %d bull and %d cow", countBulls, countCows);
            } else if (countBulls > 1 && (countCows == 1 || countCows == 0)) {
                System.out.printf("Grade: %d bulls and %d cow", countBulls, countCows);
            } else if (countBulls == 1 && countCows == 1) {
                System.out.printf("Grade: %d bull and %d cow", countBulls, countCows);
            } else if ((countBulls == 1 || countBulls == 0) && countCows > 1) {
                System.out.printf("Grade: %d bull and %d cows", countBulls, countCows);
            } else if (countBulls > 1 && countCows > 1) {
                System.out.printf("Grade: %d bulls and %d cows", countBulls, countCows);
            }
            turn++;
        }
    }

    private static String convertToString(int[] secretNumber) {
        for (int number : secretNumber) {
            SB.append(number);
        }
        String plainNumber = SB.toString();
        return plainNumber;
    }

    public static int[] generateSecretNumber() {
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            digits.add(i);
        }

        Collections.shuffle(digits);

        int[] secretNumber = new int[NUMBER_LENGTH];
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            secretNumber[i] = digits.get(i);
        }

        return secretNumber;
    }

    private static int[] newGeneratedSecretNumber(int length, int possibleCharacter) {
        List<Object> digits = new ArrayList<>();
        for (int i = 0; i < possibleCharacter; i++) {
            if (i < 10) {
                digits.add(i);
            } else {
                char ch = (char) ('a' + i - 10);
                digits.add(ch);
            }
        }

        Collections.shuffle(digits);

        int[] result = new int[length];

        if (length > 36 ) {
            System.out.printf("Error: can't generate a secret number with a length of %d" +
                    " because there aren't enough unique digits.", length);
        } else {
            for (int i = 0; i < length; i++) {
                Object digit = digits.get(i);
                if (digit instanceof Integer) {
                    result[i] = (int) digit;
                } else if (digit instanceof Character) {
                    result[i] = Character.getNumericValue((char) digit);
                }
            }
        }
        return result;
    }

    public static int[] getGuessedNumber() {
        String input = SCANNER.nextLine();
        int[] guessedNumber = new int[NUMBER_LENGTH];

        for (int i = 0; i < NUMBER_LENGTH; i++) {
            guessedNumber[i] = Character.getNumericValue(input.charAt(i));
        }

        return guessedNumber;
    }

    public static int[] newGetGuessedNumber() {
        String input = SCANNER.nextLine();
        int[] guessedNumber = new int[input.length()];

        for (int i = 0; i < guessedNumber.length; i++) {
            guessedNumber[i] = Character.getNumericValue(input.charAt(i));
        }

        return guessedNumber;
    }

    public static int countBulls(int[] guess, int[] secretNumber) {
        int countBulls = 0;
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            if (guess[i] == secretNumber[i]) {
                countBulls++;
            }
        }

        return countBulls;
    }

    public static int newCountBulls(int[] guess, int[] secretNumber, int length) {
        if ( guess.length == 0 || secretNumber.length == 0 || length == 0) {
            return 0;
        }

        int countBulls = 0;
        int minLength = Math.min(guess.length, secretNumber.length);
        for (int i = 0; i < minLength && i <length; i++) {
            if (guess[i] == secretNumber[i]) {
                countBulls++;
            }
        }

        return countBulls;
    }

    public static int countCows(int[] guess, int[] secretNumber) {
        int cows = 0;
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            for (int j = 0; j < NUMBER_LENGTH; j++) {
                if (guess[i] == secretNumber[j] && i != j) {
                    cows++;
                }
            }
        }

        return cows;
    }

    public static int newCountCows(int[] guess, int[] secretNumber, int length) {
        if (guess.length == 0 || secretNumber.length == 0) {
            return 0;
        }

        int cows = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (guess[i] == secretNumber[j] && i != j) {
                    cows++;
                }
            }
        }

        return cows;
    }
}
