import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Day1 {
    public static void main(String[] args) throws FileNotFoundException {
        File ilMioFile = new File("./input.txt");
        Scanner in = new Scanner(ilMioFile);    
        // Input
        String[] mieStringhe = new String[5000];
        int cont = 0;
        while(in.hasNextLine()) {
            mieStringhe[cont++] = in.nextLine();
        }

        int[] rotazioni = new int[cont];
        // Elaborazione
        for (int i = 0; i < cont; i++) {
            char carattere = mieStringhe[i].charAt(0);
            String sottostringaNumero = mieStringhe[i].substring(1);

            // Stringa -> numero?
            int x = Integer.parseInt(sottostringaNumero);

            if(carattere == 'R') 
                rotazioni[i] = x;
            else 
                rotazioni[i] = -x;
        }


        int posizione = 50;
        int risultato = 0;
        for (int i = 0; i < cont; i++) {
            posizione = (posizione + rotazioni[i]) % 100;
            if (posizione == 0) 
                risultato++;
        }

        System.out.println(risultato);

        System.out.println("==================================================");

        posizione = 50;
        risultato = 0;
        for (int i = 0; i < cont; i++) {
            
            System.out.println("POS " + posizione);
            System.out.println("RES " + risultato);
            int rotaz = rotazioni[i];
            System.out.println("ROT " + rotaz);
            
            if(rotaz > 0) {
                posizione = posizione + rotaz;
                risultato += posizione / 100;
                posizione = posizione % 100;
            }else {
                int tmp = posizione;
                posizione = posizione + rotaz;

                if(posizione > 0) 
                    continue;
                
                if(tmp == 0) {
                    risultato--;
                }

                if(posizione == 0) {
                    risultato += 1;
                    continue;
                }
                risultato += posizione/(-100) + 1;
                posizione = posizione % 100;
                posizione += 100;
                posizione %= 100;
            }
            System.out.println("-----");
        }

        System.out.println(risultato);
    }
}
