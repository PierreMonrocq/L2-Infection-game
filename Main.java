package jeuInfection;

import javafx.util.Pair;

/**
 *
 * @author Kevin LEUCHART & Pierre MONROCQ
 *
 * Classe principale, ex�cution du programme.
 */
public class Main {

    public static int nbMachines = 5;
    public static int nbInfectionDebut = 1; //doit etre inférieur au nombre de machines
    public static double probaLien = 1;
    public static int dDef = 10;
    public static int dAtt = 10;
    public static boolean alphabeta = false;
    public static int noeudParcourue = 0;

    public static void main(String[] args) {
        Etat etat = new Etat(nbMachines, probaLien);  //creation des machines et des liens
        System.out.println();
        etat.firstInfection(nbInfectionDebut);  //premiere infection
        System.out.println(etat);//affichage de l'etat des machines, infecté ou non.
        System.out.println(etat.graph.mat);//affichage des liens entre les machines.
        long debut = System.currentTimeMillis();
        if (!alphabeta) { // MinMax
            while (!etat.isFinished()) {
                etat.player = "MAX";
                Pair<Integer, Etat> c = IA.minmaxDef(etat, dDef);
                etat = c.getValue();
                System.out.println("Défense");
                System.out.println(etat);
                System.out.println(etat.graph.mat);
                etat.player = "MAX";
                c = IA.minmaxAtt(etat, dAtt);
                etat = c.getValue();
                System.out.println("Attaque");
                System.out.println(etat);
                System.out.println(etat.graph.mat);
            }
            System.out.println();
            System.out.println("Nombre de noeuds parcourus par l'utilisation de MinMax : " + noeudParcourue);
        } else { //AlphaBeta
            int a = Integer.MAX_VALUE;
            int b = Integer.MIN_VALUE;
            while (!etat.isFinished()) {
                etat.player = "MAX";
                Pair<Integer, Etat> c = IA.alphabetaDef(etat, a, b, dDef);
                etat = c.getValue();
                System.out.println("Defense");
                System.out.println(etat);
                System.out.println(etat.graph.mat);
                etat.player = "MAX";
                c = IA.alphabetaAtt(etat, a, b, dAtt);
                etat = c.getValue();
                System.out.println("Attaque");
                System.out.println(etat);
                System.out.println(etat.graph.mat);
            }
            System.out.println();
            System.out.println("Nombre de noeuds parcourus par l'utilisation d'AlphaBeta : " + noeudParcourue);
        }
        System.out.println();
        System.out.println(etat);
        System.out.println(etat.graph.mat);
        System.out.println(etat.afficherFin());
        System.out.println("Temps execution: " + (double) (System.currentTimeMillis() - debut) / 1000 + "s");
    }

}
