package tp2;

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
        String allDigits = "0123456789";
        String reversibleDigits = "01258";
        HashMap map = new HashMap<Character, Integer>();

        //initialiser tous les compteurs à 0
        for (int i = 0; i < allDigits.length(); i++) {
            map.put(allDigits.charAt(i), 0);
        }

        if (len % 2 == 0) {//dans le cas ou on a un nombre pair de chiffres
            for (int i = 0; i < len; i++) {

                //compter le nombre qu'on a de chaque chiffre
                char nb = listOfNumbers.charAt(i);
                if (!isNumberLigal(nb)) {
                    return false;
                } else {
                    map.put(nb, (int) map.get(nb) + 1);
                }
            }

            // on doit avoir un nombre pair de bon nombre et un nombre de 6=au nombre de 9 pour que ça marche
            for (int i = 0; i < reversibleDigits.length(); i++) {
                if ((int) map.get(reversibleDigits.charAt(i)) % 2 != 0) {
                    return false;
                }
            }
            return (int) map.get('6') == (int) map.get('9');

        } else {//dans le cas ou on a un nombre impair de chiffres
            boolean centerIsTaken = false;
            for (int i = 0; i < len; i++) {

                //compter le nombre qu'on a de chaque chiffre
                char nb = listOfNumbers.charAt(i);
                if (!isNumberLigal(nb)) {
                    return false;
                } else {
                    map.put(nb, (int) map.get(nb) + 1);
                }

            }
            // on doit avoir un nombre pair de bon nombre et un nombre de 6=au nombre de 9 pour que ça marche
            for (int i = 0; i < reversibleDigits.length(); i++) {
                if ((int) map.get(reversibleDigits.charAt(i)) % 2 != 0) {
                    if (!centerIsTaken) {
                        centerIsTaken = true;
                    } else {
                        return false;
                    }
                }
            }
            if ((int) map.get('6') != (int) map.get('9')) {
                if (!centerIsTaken && (int) map.get('6') != 0 && (int) map.get('9') != 0) {
                    centerIsTaken = true;
                } else {
                    return false;
                }
            }
            return true;
        }
    }
}
