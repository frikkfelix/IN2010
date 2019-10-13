package me.frikk.oblig0;

// Oppgave 1
public class Fibonacci {
    public static void main(String[] args) throws Exception {
        Fibonacci f = new Fibonacci();
        long argument = Integer.parseInt(args[0]);
        if (argument < 0) argument = 0;
        System.out.println(String.format("f(%d) = %d", argument, f.fibonacci(argument)));

        for (int i = 41; i <= 50; i++) {
            long startTime = System.nanoTime();
            System.out.println(String.format("f(%d) = %d", i, f.fibonacci(i)));
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000);
        }   
    }

    long fibonacci(long n) {
        if (n <= 1) {
            return n;
        } else {
            return (fibonacci(n - 1) + fibonacci(n - 2));
        }
    }
}