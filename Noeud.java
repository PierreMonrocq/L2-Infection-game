package jeuInfection;

import java.util.HashMap;

/**
 *
 * @author Kevin LEUCHART & Pierre MONROCQ
 * 
 * D�finition d'une machine (sous forme d'un noeud).
 */

public class Noeud {
    
    
    HashMap<Integer, Boolean> noeud;
    public int id;
    
    public Noeud(int id){
        this.noeud = new HashMap<>();
        this.noeud.put(id, false);
        this.id=id;
    }
    
    public Noeud(int id, boolean infection){
        this.noeud = new HashMap<>();
        this.noeud.put(id, infection);
        this.id=id;
    }
    
    public int getIdNoeud(){
        return this.id;
    }
    
    public void setIdNoeud(int id){
        this.id=id;
    }
    
    public boolean isInfecte(){
        return this.noeud.get(id);
    }
   
    public void setInfecte(boolean infecte){
        this.noeud.put(this.id, infecte);
    }
    
    @Override
    public String toString(){
        if(this.noeud.get(this.id)){
          return ""+this.id+ " -> infectée";
        }else{
          return ""+this.id+ " -> non infectée";
        }
    }
}
