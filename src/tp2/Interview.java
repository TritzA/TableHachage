package tp2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

public final class Interview {

    /**
     * TODO Worst Case : O(n)
     * Is valid if you flip the number upside down.
     */
    public static boolean isValidFlipped(String listOfNumbers) {
        if (isIrreversible(listOfNumbers)) {
            return false;
        }
        for (int i = listOfNumbers.length() - 1; 0 <= i; i--) {
            char c = listOfNumbers.charAt(listOfNumbers.length() - 1 - i);
            if (!(Character.getNumericValue(listOfNumbers.charAt(i)) == 6 && Character.getNumericValue(c) == 9) ||
                    !(Character.getNumericValue(listOfNumbers.charAt(i)) == 9 && Character.getNumericValue(c) == 6)) {
                if (listOfNumbers.charAt(i) != c) {
                    return false;
                } else if (Character.getNumericValue(listOfNumbers.charAt(0)) == 6)
                    return false;
                else if (Character.getNumericValue(listOfNumbers.charAt(0)) == 9)
                    return false;
            }
        }
        return true;
    }

    private static boolean isIrreversible(String listOfNumbers) {
        if (listOfNumbers.contains("3"))
            return true;
        else if (listOfNumbers.contains("4"))
            return true;
        else return listOfNumbers.contains("7");
    }

    private static boolean isNumberLigal(char nb) {
        if (nb == '3') {
            return false;
        } else if (nb == '4') {
            return false;
        }
        return nb != '7';
    }

    /**
     * TODO Worst Case : O(n)
     * Could be valid if you try to flip the number upside down with one of the combinations.
     */
    public static boolean isValidFlippedWithPermutation(String listOfNumbers) {
        int len = listOfNumbers.length();
        int zero = 0, un = 0, deux = 0, trois = 0, quatre = 0, cinq = 0, six = 0, sept = 0, huit = 0, neuf = 0;


        if (len % 2 == 0) {
            for (int i = 0; i < len; i++) {

                //compter le nombre qu'on a de chaque chiffre
                char nb = listOfNumbers.charAt(i);
                if (!isNumberLigal(nb)) {
                    return false;
                } else if (nb == '0') {
                    zero++;
                } else if (nb == '1') {
                    un++;
                } else if (nb == '2') {
                    deux++;
                } else if (nb == '5') {
                    cinq++;
                } else if (nb == '6') {
                    six++;
                } else if (nb == '8') {
                    huit++;
                } else if (nb == '9') {
                    neuf++;
                }
            }

            // on doit avoir un nombre pair de bon nombre et un nombre de 6=au nombre de 9 pour que ça marche
            if (zero % 2 != 0) {
                return false;
            } else if (un % 2 != 0) {
                return false;
            } else if (deux % 2 != 0) {
                return false;
            } else if (cinq % 2 != 0) {
                return false;
            } else if (huit % 2 != 0) {
                return false;
            } else return six == neuf;
        } else {
            boolean centerIsTaken = false;
            for (int i = 0; i < len; i++) {

                //compter le nombre qu'on a de chaque chiffre
                char nb = listOfNumbers.charAt(i);
                if (!isNumberLigal(nb)) {
                    return false;
                } else if (nb == '0') {
                    zero++;
                } else if (nb == '1') {
                    un++;
                } else if (nb == '2') {
                    deux++;
                } else if (nb == '5') {
                    cinq++;
                } else if (nb == '6') {
                    six++;
                } else if (nb == '8') {
                    huit++;
                } else if (nb == '9') {
                    neuf++;
                }
            }
            // on doit avoir un nombre pair de bon nombre et un nombre de 6=au nombre de 9 pour que ça marche
            if (zero % 2 != 0) {
                if (!centerIsTaken) {
                    centerIsTaken = true;
                } else {
                    return false;
                }
            } else if (un % 2 != 0) {
                if (!centerIsTaken) {
                    centerIsTaken = true;
                } else {
                    return false;
                }
            } else if (deux % 2 != 0) {
                if (!centerIsTaken) {
                    centerIsTaken = true;
                } else {
                    return false;
                }
            } else if (cinq % 2 != 0) {
                if (!centerIsTaken) {
                    centerIsTaken = true;
                } else {
                    return false;
                }
            } else if (huit % 2 != 0) {
                if (!centerIsTaken) {
                    centerIsTaken = true;
                } else {
                    return false;
                }
            } else if (six != neuf) {
                if (!centerIsTaken && six != 0 && neuf != 0) {
                    centerIsTaken = true;
                } else {
                    return false;
                }
            }
            return true;
        }
        /*
        HashMap map = new HashMap<Integer, String>();
         */
    }
}
// (0,0) (1,1) (2,5) (5,2) (6,9) (8,8)