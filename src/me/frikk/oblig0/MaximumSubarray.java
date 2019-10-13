package me.frikk.oblig0;

// Oppgave 3
// algoritmene er utarbeidet v.h.a. pseudokode i læreboka avsnitt 1.3
class MaximumSubarray {
    public static void main(String[] args) {
        MaximumSubarray ms = new MaximumSubarray();
        int[] n = {-2, -4, 3, -1, 5, 6, -7, -2, 4, -3, 2};
        int size = n.length;
        System.out.println(ms.maxSubCubic(n, size));
        System.out.println(ms.maxSubQuadratic(n, size));
        System.out.println(ms.maxSubLinear(n, size));
    }

    int maxSubCubic(int[] arr, int size) {
        int currentMax = 0;

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                int partialSum = 0;
                for (int k = i; k < j; k++) {
                    partialSum += arr[k]; 
                }
                if (partialSum > currentMax) {
                    currentMax = partialSum;
                }
            }
        }
        return currentMax;
    }

    int maxSubQuadratic(int[] arr, int size) {
        int[] solution = new int[size + 1];
        solution[0] = 0;

        for (int i = 1; i < size; i++) {
            solution[i] = solution[i-1] + arr[i - 1];
        }

        int currentMax = 0;
        for (int i = 1; i < size; i++) {
            for (int j = i; j < size; j++) {
                int partialSum = solution[j] - solution[i-1];
                if (partialSum > currentMax) {
                    currentMax = partialSum;
                }
            }
        }
        return currentMax;
    }

    int maxSubLinear(int[] arr, int size) {
        int[] maximum = new int[size + 1];
        maximum[0] = 0;

        for (int i = 1; i < size; i++) {
            maximum[i] = Math.max(0, maximum[i-1] + arr[i - 1]);
        }

        int currentMax = 0;
        for (int i = 1; i < size; i++) {
            currentMax = Math.max(currentMax, maximum[i]);
        }
        return currentMax;
    }

}
