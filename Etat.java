package jeuInfection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.util.Pair;

/**
 *
 * @author Kevin LEUCHART & Pierre MONROCQ
 */
public class Etat {

    private ArrayList<Noeud> listeN = new ArrayList<>();
    private int nbMachine;
    public Graph graph;
    private ArrayList<Integer> noeudsAInfecter;
    private ArrayList<Integer> noeudsNonInfecte;
    public String player;

    public Etat(int m, double proba) {
        this.nbMachine = m;
        this.graph = new Graph(m);
        this.graph.Init(m, proba);
        for (int i = 0; i < m; i++) {
            Noeud n = new Noeud(i + 1);
            this.listeN.add(n);
        }
    }

    public Etat(Graph graph, ArrayList<Noeud> noeuds) {
        this.graph = graph;
        this.listeN = noeuds;
    }

    @Override
    public String toString() {
        return this.listeN.toString();
    }

    public void firstInfection(int nb) {
        for (int i = 0; i < nb; i++) {
            Random rd = new Random();
            int probinfection = rd.nextInt(this.nbMachine) + 1;
            while (this.listeN.get(probinfection - 1).isInfecte()) { //permet de ne pas pirater deux fois la meme machine
                rd = new Random();
                probinfection = rd.nextInt(this.nbMachine) + 1;
            }
            this.listeN.get(probinfection - 1).setInfecte(true);
            System.out.println("la machine " + probinfection + " est piratée.");
        }
        this.getAttack();
        this.getDefense();
    }

    public Etat playAttack(Pair<Integer, ArrayList<Integer>> c, String player) {      //tour de l'attaquant, la machine c va etre infectee
        ArrayList<Noeud> newliste = cloneNoeud();
        Graph newGraph = cloneGraph();
        Etat res = new Etat(newGraph, newliste);
        res.setPlayer(player);

        for (int i = 0; i < res.listeN.size(); i++) {
            if (res.listeN.get(i).id == c.getKey()) {
                res.listeN.get(i).setInfecte(true);//infecte une machine
            }
        }

        res.getAttack();
        return res;

    }

    public Etat playDefense(Pair<Integer, ArrayList<Integer>> c, String player) { //tour du défenseur, la machine c va avoir des liens coupees
        ArrayList<Noeud> newListe = cloneNoeud();
        Graph newGraph = cloneGraph();
        Etat res = new Etat(newGraph, newListe);
        res.setPlayer(player);

        for (Integer num : c.getValue()) { //coupe les liens de la premiere valeur de la paire avec les machines de la deuxieme valeur de la paire
            for (int i = 0; i < res.graph.mat.get(c.getKey()).size(); i++) {
                if (res.graph.mat.get(c.getKey()).get(i).equals(num)) {
                    res.graph.mat.get(c.getKey()).remove(i);
                }
            }
        }

        for (Integer num : c.getValue()) { //pour chaque machine dans la deuxieme valeur de la paire, coupe le lien avec la premiere valeur de la paire
            for (int i = 0; i < res.graph.mat.get(num).size(); i++) {
                if (res.graph.mat.get(num).get(i).equals(c.getKey())) {
                    res.graph.mat.get(num).remove(i);
                }
            }
        }

        res.getAttack();
        return res;
    }

    public ArrayList<Pair<Integer, ArrayList<Integer>>> getAttack() { //retourne les machines que l'on peut infecter
        ArrayList<Pair<Integer, ArrayList<Integer>>> res = new ArrayList<>();
        ArrayList<Integer> infecte = new ArrayList<>();
        for (int i = 0; i < this.listeN.size(); i++) {
            if (this.listeN.get(i).isInfecte()) {
                infecte.add(this.listeN.get(i).getIdNoeud()); //verifie toutes les machines deja infectées
            }
        }

        this.noeudsAInfecter = new ArrayList<>();
        for (Integer num : infecte) { //regarde les machines qui peuvent etre infectées grace au lien des machines deja infectées
            for (int j = 0; j < this.graph.mat.get(num).size(); j++) {
                if (!infecte.contains(this.graph.mat.get(num).get(j))) {
                    if (!this.noeudsAInfecter.contains(this.graph.mat.get(num).get(j))) {
                        this.noeudsAInfecter.add(this.graph.mat.get(num).get(j));
                        Pair<Integer, ArrayList<Integer>> lien = new Pair(this.graph.mat.get(num).get(j), null);
                        res.add(lien);
                    }
                }
            }
        }
        return res;
    }

    public ArrayList<Pair<Integer, ArrayList<Integer>>> getDefense() { //retourne tous les liens qui peuvent etre coupé
        ArrayList<Pair<Integer, ArrayList<Integer>>> res = new ArrayList<>();
        this.noeudsNonInfecte = new ArrayList<>();
        for (int i = 0; i < this.listeN.size(); i++) {
            if (!this.listeN.get(i).isInfecte()) {
                if (!this.graph.mat.get(i + 1).isEmpty()) {
                    this.noeudsNonInfecte.add(listeN.get(i).getIdNoeud());
                    ArrayList<Integer> test = this.graph.mat.get(i + 1);//valeurs à tester
                    ArrayList<ArrayList<Integer>> aCouper = powerset(test); //Chargement de la liste des liens possibles à couper
                    for (ArrayList<Integer> l : aCouper) {
                        Pair<Integer, ArrayList<Integer>> lien = new Pair(i + 1, l); //pour chaque machine non infectée, retourne la machine aussi que tout les couplages de liens possibles
                        res.add(lien);

                    }
                }
            }
        }
        return res;
    }

    public Integer getValueDef() { //retourne la valeur de la défense (nombre de machines non infectées)
        int nbNonInfecte = 0;
        for (int i = 0; i < this.listeN.size(); i++) {
            if (!this.listeN.get(i).isInfecte()) {
                nbNonInfecte++;
            }
        }
        return nbNonInfecte;
    }

    public Integer getValueAtt() { //retourne la valeur de l'attaque (nombre de machines infectées)
        int nbInfecte = 0;
        for (int i = 0; i < this.listeN.size(); i++) {
            if (this.listeN.get(i).isInfecte()) {
                nbInfecte++;
            }
        }
        return nbInfecte;
    }

    public String getPlayer() {
        return this.player;
    }

    public void setPlayer(String player) {//association du joueur (MIN ou MAX)
        this.player = player;
    }

    public boolean isFinished() {//Si la liste des noeuds à infecter est vide alors la partie est terminée
        return (this.noeudsAInfecter.isEmpty());
    }

    public Graph cloneGraph() {//permet d'associer un graph à un nouvel objet (et donc de faire un nouvel etat)
        HashMap<Integer, ArrayList<Integer>> newLien = new HashMap<>();
        for (Map.Entry<Integer, ArrayList<Integer>> lien : this.graph.mat.entrySet()) {
            newLien.put(lien.getKey(), new ArrayList<Integer>(lien.getValue()));
        }
        Graph newgraph = new Graph(this.nbMachine, newLien);
        return newgraph;
    }

    public ArrayList<Noeud> cloneNoeud() {//permet d'associer les noeuds à de nouveaux objets (même principe que pour les graphs)
        ArrayList<Noeud> newListe = new ArrayList<>();
        for (Noeud noeud : this.listeN) {
            Noeud newnoeud = new Noeud(noeud.id, noeud.isInfecte());
            newListe.add(newnoeud);
        }
        return newListe;
    }

    public String afficherFin() {//affichage de la fin du jeu
        System.out.println();
        int nbPirate = 0; //machine piratées
        int nbNonPirate = 0;//machine non piratées
        for (int i = 0; i < this.listeN.size(); i++) {
            if (this.listeN.get(i).isInfecte()) {
                nbPirate += 1;
            } else {
                nbNonPirate += 1;
            }
        }

        String msg = "";
        if (nbNonPirate > nbPirate) {
            msg = "Le défenseur a gagné, avec " + nbNonPirate + " machines non piratées et " + nbPirate + " machines piratées.";
        } else if (nbNonPirate < nbPirate) {
            msg = "L'attaquant a gagné, avec " + nbPirate + " machines piratées et " + nbNonPirate + " machines non piratées.";
        } else {
            msg = "Il y a égalité, avec " + nbPirate + " machines  piratées et " + nbNonPirate + " machines non piratées.";
        }

        return msg;
    }

    public static ArrayList<ArrayList<Integer>> powerset(ArrayList<Integer> list) {//https://rosettacode.org/wiki/Power_set#Java algorithme 
        ArrayList<ArrayList<Integer>> ps = new ArrayList<>();
        ps.add(new ArrayList<>());   // ajoute la liste vide

        // pour chaque objet de la liste originale
        for (Integer item : list) {
            ArrayList<ArrayList<Integer>> newPs = new ArrayList<>();

            for (ArrayList<Integer> subset : ps) {
                // copie tout les sous-ensemble du powerset
                newPs.add(subset);

                // ajoute les sous ensemble ainsi que l'element lui meme
                ArrayList<Integer> newSubset = new ArrayList<>(subset);
                newSubset.add(item);
                newPs.add(newSubset);
            }

            // le powerset est maintenant un powerset de list.subList(0, list.indexOf(item)+1)
            ps = newPs;
        }
        ps.remove(0);
        return ps;
    }
}
