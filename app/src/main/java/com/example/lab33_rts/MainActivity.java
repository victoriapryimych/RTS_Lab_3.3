package com.example.lab33_rts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button calc = findViewById(R.id.button);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = ((TextView) findViewById(R.id.coefs)).getText().toString();
                int y = Integer.parseInt(((TextView) findViewById(R.id.y)).getText().toString());
                int n = Integer.parseInt(((TextView) findViewById(R.id.n)).getText().toString());
                str = str.replace(" ", "");
                String[] strCoef = str.split(",");
                int[] x = new int[strCoef.length];
                for (int i = 0; i < strCoef.length; i++)
                    x[i] = Integer.parseInt(strCoef[i]);
                String res = genetic(x, y, n);
                TextView resOutput = findViewById(R.id.Result);
                resOutput.setText(res);
            }
        });
    }

    public static String genetic(int[] x, int y, int n) {
        Random rand = new Random();
        int[][] population = new int[n][x.length];
        int[] roots;
        boolean notGenerated = true;
        int mutNum, mutSum = 0;
        String resStr = "";

        while (true) {
            //generate start population
            if(notGenerated)
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < x.length; j++)
                    population[i][j] = rand.nextInt(y / 2);
                notGenerated = false;
            }
            //delta calculation
            int[] deltas = new int[population.length];
            for (int i = 0; i < deltas.length; i++) {
                deltas[i] = delta(x, y, population[i]);
                if (deltas[i] == 0) {
                    roots = population[i];
                    resStr = "a = " + roots[0] + "\nb = " + roots[1] + "\nc = " +
                            roots[2] + "\nd = " + roots[3] + "\nMutations = " + mutSum;
                    return resStr;
                }
            }

            //search parents
            int[] min = {deltas[0], deltas[1]};
            int[] par1 = population[0];
            int[] par2 = population[1];
            for (int i = 2; i < deltas.length; i++) {
                if (deltas[i] < min[0]) {
                    min[0] = deltas[i];
                    par1 = population[i];
                }
                if (deltas[i] != min[0] && deltas[i] < min[1]) {
                    min[1] = deltas[i];
                    par2 = population[i];
                }
            }
            //crossover
            int[][] child = new int[2][x.length];
            for (int j = 0; j < x.length; j++) {
                child[0] = par1;
                child[1] = par2;
                for (int i = j; i < x.length - 1; i++) {
                    int a = child[0][i];
                    child[0][i] = child[1][i];
                    child[1][i] = a;
                    if (delta(x, y, child[0]) == 0) {
                        roots = child[0];
                        resStr = "a = " + roots[0] + "\nb = " + roots[1] + "\nc = " +
                                roots[2] + "\nd = " + roots[3] + "\nMutations = " + mutSum;
                        return resStr;
                    }
                    if (delta(x, y, child[1]) == 0) {
                        roots = child[1];
                        resStr = "a = " + roots[0] + "\nb = " + roots[1] + "\nc = " +
                                roots[2] + "\nd = " + roots[3] + "\nMutations = " + mutSum;
                        return resStr;
                    }
                }
            }
            //mutations
            mutNum = rand.nextInt(n);
            mutSum += mutNum;
            for (int j = 0; j < mutNum; j++) {
                population[rand.nextInt(population.length - 1)][x.length - 1] = rand.nextInt(y / 2);
            }
        }
    }

    private static int delta(int[] x, int y, int[] coef) {
        int delta = 0;
        for (int i = 0; i < x.length; i++)
            delta += x[i] * coef[i];
        delta = Math.abs(y - delta);
        return delta;
    }
}
