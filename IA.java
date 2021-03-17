package jeuInfection;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import java.util.ArrayList;
import javafx.util.Pair;
import static jeuInfection.Main.noeudParcourue;

/**
 *
 * @author Kevin LEUCHART & Pierre MONROCQ
 * 
 * Minmax, alphabeta et negamax.
 */
public class IA {

    public static Pair<Integer, Etat> minmaxDef(Etat s, int d) {
        noeudParcourue++;
        if (d == 0 || s.isFinished()) { //lorsque la profondeur de raisonnement est atteinte ou que la partie est finis, retourne la valeur de la defense et l'etat en cours
            Pair<Integer, Etat> res = new Pair(s.getValueDef(), s);
            return res;
        }
        if (s.getPlayer().equals("MAX")) {
            int m = Integer.MIN_VALUE;
            Etat c = null;
            for (Pair<Integer, ArrayList<Integer>> cprime : s.getDefense()) { //pour chaque coup possible(cprime) de la defense, on calcule ce que va faire l'autre
                Pair<Integer, Etat> v = minmaxDef(s.playDefense(cprime, "MIN"), d - 1);
                if (v.getKey() > m) { //Si la valeur du coup est superieur à celle du coup precedent, on la remplace jusqu'à avoir fait tout les coups
                    m = v.getKey();
                    c = s.playDefense(cprime, null);
                }
            }
            Pair<Integer, Etat> res = new Pair(m, c);
            return res; //on renvoie le meilleur coup à jouer

        } else if (s.getPlayer().equals("MIN")) {
            int m = Integer.MAX_VALUE;
            Etat c = null;
            for (Pair<Integer, ArrayList<Integer>> cprime : s.getAttack()) { //pour chaque coup possible(cprime) de l'attaque, on calcule ce que va faire l'autre
                Pair<Integer, Etat> v = minmaxDef(s.playAttack(cprime, "MAX"), d - 1);
                if (v.getKey() < m) { //Si la valeur du coup est inferieure à celle du coup precedent, on la remplace jusqu'à avoir fait tout les coups
                    m = v.getKey();
                    c = s.playAttack(cprime, null);
                }
            }
            Pair<Integer, Etat> res = new Pair(m, c);
            return res; //on renvoie le coup le plus rentable pour l'adversaire, qu'on va essayer d'eviter
        }
        return null;
    }

    public static Pair<Integer, Etat> minmaxAtt(Etat s, int d) {
        noeudParcourue++;
        if (d == 0 || s.isFinished()) { //lorsque la profondeur de raisonnement est atteinte ou que la partie est finis, retourne la valeur de l'attaque et l'etat en cours
            Pair<Integer, Etat> res = new Pair(s.getValueAtt(), s);
            return res;
        }
        if (s.getPlayer().equals("MAX")) {
            int m = Integer.MIN_VALUE;
            Etat c = null;
            for (Pair<Integer, ArrayList<Integer>> cprime : s.getAttack()) { //pour chaque coup possible(cprime) de l'attaque, on calcule ce que va faire l'autre
                Pair<Integer, Etat> v = minmaxAtt(s.playAttack(cprime, "MIN"), d - 1);
                if (v.getKey() > m) { //Si la valeur du coup est superieur à celle du coup precedent, on la remplace jusqu'à avoir fait tout les coups
                    m = v.getKey();
                    c = s.playAttack(cprime, null); //on renvoie le meilleur coup à jouer
                }
            }

            Pair<Integer, Etat> res = new Pair(m, c);
            return res;
        } else if (s.getPlayer().equals("MIN")) {
            int m = Integer.MAX_VALUE;
            Etat c = null;
            for (Pair<Integer, ArrayList<Integer>> cprime : s.getDefense()) { //pour chaque coup possible(cprime) de la defense, on calcule ce que va faire l'autre
                Pair<Integer, Etat> v = minmaxAtt(s.playDefense(cprime, "MAX"), d - 1);
                if (v.getKey() < m) { //Si la valeur du coup est inferieur à celle du coup precedent, on la remplace jusqu'à avoir fait tout les coups
                    m = v.getKey();
                    c = s.playDefense(cprime, null);
                }

            }
            Pair<Integer, Etat> res = new Pair(m, c);
            return res; //on renvoie le coup le plus rentable pour l'adversaire, qu'on va essayer d'eviter

        }
        return null;
    }

    public static Pair<Integer, Etat> alphabetaDef(Etat s, int alpha, int beta, int d) {
        noeudParcourue++;
        if (d == 0 || s.isFinished()) { //lorsque la profondeur de raisonnement est atteinte ou que la partie est finis, retourne la valeur de la defense et l'etat en cours
            Pair<Integer, Etat> res = new Pair(s.getValueDef(), s);
            return res;
        } else {
            if (s.getPlayer().equals("MAX")) {
                Etat c = null;
                for (Pair<Integer, ArrayList<Integer>> cprime : s.getDefense()) { //pour chaque coup possible(cprime) de la defense, on calcule ce que va faire l'autre
                    alpha = max(alpha, alphabetaDef(s.playDefense(cprime, "MIN"), alpha, beta, d - 1).getKey());
                    c = s.playDefense(cprime, null);
                    if (alpha >= beta) {  //Si la valeur du coup est supérieur à beta, on renvoie directement ce coup sans calculer tout les autres coups possibles        
                        Pair<Integer, Etat> res = new Pair(alpha, c);
                        return res;
                    }
                }
                Pair<Integer, Etat> res = new Pair(alpha, c);
                return res; //Si on n'a pas trouvé de valeur supérieure à beta, on renvoie le coup le meilleur coup possible
            } else {
                if (s.getPlayer().equals("MIN")) {
                    Etat c = null;
                    for (Pair<Integer, ArrayList<Integer>> cprime : s.getAttack()) { //pour chaque coup possible(cprime) de l'attaque, on calcule ce que va faire l'autre
                        beta = min(beta, alphabetaDef(s.playAttack(cprime, "MAX"), alpha, beta, d - 1).getKey());
                        c = s.playAttack(cprime, null);
                        if (alpha >= beta) {  //Si la valeur du coup est inférieure à alpha, on renvoie directement ce coup sans calculer tout les autres coups possibles      
                            Pair<Integer, Etat> res = new Pair(beta, c);
                            return res;
                        }
                    }
                    Pair<Integer, Etat> res = new Pair(beta, c);
                    return res; //Si on n'a pas trouvé de valeur inferieur à alpha, on renvoie le coup le plus rentable pour l'adversaire, qu'on va essayer d'eviter
                }
            }

        }
        return null;
    }

    public static Pair<Integer, Etat> alphabetaAtt(Etat s, int alpha, int beta, int d) {
        noeudParcourue++;
        if (d == 0 || s.isFinished()) { //lorsque la profondeur de raisonnement est atteinte ou que la partie est finis, retourne la valeur de l'attaque et l'etat en cours
            Pair<Integer, Etat> res = new Pair(s.getValueAtt(), s);
            return res;
        } else {
            if (s.getPlayer().equals("MAX")) {
                Etat c = null;
                for (Pair<Integer, ArrayList<Integer>> cprime : s.getAttack()) { //pour chaque coup possible(cprime) de l'attaque, on calcule ce que va faire l'autre
                    alpha = max(alpha, alphabetaAtt(s.playAttack(cprime, "MIN"), alpha, beta, d - 1).getKey());
                    c = s.playAttack(cprime, null);
                    if (alpha >= beta) { //Si la valeur du coup est supérieur à beta, on renvoie directement ce coup sans calculer tout les autres coups possibles   
                        Pair<Integer, Etat> res = new Pair(alpha, c);
                        return res;
                    }
                }
                Pair<Integer, Etat> res = new Pair(alpha, c);
                return res; //Si on n'a pas trouvé de valeur supérieure à beta, on renvoie le coup le meilleur coup possible
            } else {
                if (s.getPlayer().equals("MIN")) {
                    Etat c = null;
                    for (Pair<Integer, ArrayList<Integer>> cprime : s.getDefense()) { //pour chaque coup possible(cprime) de la defense, on calcule ce que va faire l'autre
                        beta = min(beta, alphabetaAtt(s.playDefense(cprime, "MAX"), alpha, beta, d - 1).getKey());
                        c = s.playDefense(cprime, null);
                        if (alpha >= beta) { //Si la valeur du coup est inférieure à alpha, on renvoie directement ce coup sans calculer tout les autres coups possibles      
                            Pair<Integer, Etat> res = new Pair(beta, c);
                            return res;
                        }
                    }
                    Pair<Integer, Etat> res = new Pair(beta, c);
                    return res;  //Si on n'a pas trouvé de valeur inferieur à alpha, on renvoie le coup le plus rentable pour l'adversaire, qu'on va essayer d'eviter
                }
            }

        }
        return null;
    }

    public static Pair<Integer, Etat> negamaxDef(Etat s, int d) {
        if (d == 0 || s.isFinished()) { //lorsque la profondeur de raisonnement est atteinte ou que la partie est finis, retourne la valeur de la défense et l'etat en cours
            Pair<Integer, Etat> res = new Pair(s.getValueDef(), s);
            return res;

        } else {
            int m = Integer.MIN_VALUE;
            Etat c = null;
            for (Pair<Integer, ArrayList<Integer>> cprime : s.getDefense()) { //pour chaque coup possible(cprime) de la defense, on calcule ce que va faire l'autre
                m = max(m, -negamaxDef(s.playAttack(cprime, "MIN"), d - 1).getKey());
                c = s.playDefense(cprime, null);
            }
            Pair<Integer, Etat> res = new Pair(m, c);
            return res;
        }

    }

    public static Pair<Integer, Etat> negamaxAtt(Etat s, int d) {
        if (d == 0 || s.isFinished()) { //lorsque la profondeur de raisonnement est atteinte ou que la partie est finis, retourne la valeur de l'attaque et l'etat en cours
            Pair<Integer, Etat> res = new Pair(s.getValueAtt(), s);
            return res;

        } else {
            int m = Integer.MIN_VALUE;
            Etat c = null;
            for (Pair<Integer, ArrayList<Integer>> cprime : s.getAttack()) { //pour chaque coup possible(cprime) de l'attaque, on calcule ce que va faire l'autre
                m = max(m, -negamaxAtt(s.playAttack(cprime, "MAX"), d - 1).getKey());
                c = s.playAttack(cprime, null);
            }
            Pair<Integer, Etat> res = new Pair(m, c);
            return res;
        }

    }
}
