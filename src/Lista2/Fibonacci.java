package Lista2;

import java.util.Scanner;

public class Fibonacci {

    static long fibo(int n) {
        if (n < 2) return n;

        long a = 0, b = 1, temp;
        for (int i = 2; i <= n; i++) {
            temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite a quantidade de termos da sequência de Fibonacci: ");
        int quantidade = scanner.nextInt();

        if (quantidade < 0) {
            System.out.println("Por favor, insira um número inteiro não negativo.");
            return;
        }

        System.out.println("Sequência de Fibonacci com " + quantidade + " termos:");
        for (int i = 0; i < quantidade; i++) {
            System.out.print(fibo(i) + "\t");
        }

        scanner.close();
    }
}

