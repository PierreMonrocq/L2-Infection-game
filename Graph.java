package jeuInfection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Kevin LEUCHART & Pierre MONROCQ
 * 
 * Représentation d'un réseau de machine sous forme d'un graphe non orienté.
 */
public class Graph {

    public final int m;
    public HashMap<Integer, ArrayList<Integer>> mat;

    public Graph(int m) {
        this.m = m;
        mat = new HashMap<>();
    }

    public Graph(Integer m, HashMap<Integer, ArrayList<Integer>> lien) {
        this.m = m;
        this.mat = lien;
    }

    public void Init(int nbMachine, double proba) {

        for (int i = 1; i <= nbMachine; i++) {
            ArrayList liens = new ArrayList();
            this.mat.put(i, liens);
        }

        for (int i = 1; i <= nbMachine; i++) {
            for (int j = 1; j <= nbMachine; j++) {
                if (j != i) {
                    if (this.mat.get(i).contains(j) == false) {
                        double probaLien = Math.random();
                        if (probaLien <= proba) { //permets d'ajouter un lien seulement si la valeur alÃ©atoire est infÃ©rieure Ã  la probabilitÃ© rentrÃ©e
                            this.mat.get(i).add(j);
                            this.mat.get(j).add(i);
                        }
                    }
                }
            }
        }

        for (int z = 1; z <= nbMachine; z++) { //Si une machine n'a pas de lien (ce qui n'est pas possible), trouve un lien avec une autre machine
            if (this.mat.get(z).isEmpty() == true) {
                Random rd = new Random();
                int probVide = rd.nextInt(nbMachine) + 1;
                while (probVide == z) {
                    rd = new Random();
                    probVide = rd.nextInt(nbMachine) + 1;
                }
                this.mat.get(z).add(probVide);
                this.mat.get(probVide).add(z);
            }
        }
    }

    public void afficherGraph() {
        System.out.println(this.mat);
    }

}
