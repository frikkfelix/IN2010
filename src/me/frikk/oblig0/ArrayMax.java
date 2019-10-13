package me.frikk.oblig0;

// Oppgave 2 - oversettelse av algoritme 1.2
class ArrayMax {
    public static void main(String[] args) {
        ArrayMax am = new ArrayMax();
        int[] numbers = {2, 6, 4, 3, 9, 8, 11, 2, 43};
        int n = numbers.length;
        System.out.println(am.arrayMax(numbers, n));
    }

    int arrayMax(int[] a, int n) {
        int currentMax = a[0];

        for (int i = 0; i < n; i++) {
            if (currentMax < a[i]) {
                currentMax = a[i];
            }
        }
        return currentMax;
    }
}