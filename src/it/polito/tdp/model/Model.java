package it.polito.tdp.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.dao.EsameDAO;

public class Model {
	
	private List<Esame> listEsami;
	
	private Set<Esame> bestSubSet;
	private double mediaBest = 0.0;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		listEsami = dao.getTuttiEsami();
		
	}

	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		bestSubSet = new HashSet<>();
		
		Set<Esame> parziale = new HashSet<>();
		int livello = 0;
		
		generaTuttiSottoinsiemi(parziale, numeroCrediti, livello);
		
		return bestSubSet;
	}
	
	private void generaTuttiSottoinsiemi(Set<Esame> parziale, int numeroCrediti, int livello) {
		//casi di terminazione
		
		//caso 1: superato il numero di crediti
		int totCrediti = calcolaCrediti(parziale);
		if(totCrediti > numeroCrediti)
			return;
		//caso 2: raggiunta una possibile soluzione tot crediti == numeroCrediti
		if(totCrediti == numeroCrediti) {
			double media = calcolaMedia(parziale);
			if( media > mediaBest) {
				bestSubSet = new HashSet<>(parziale);
				mediaBest = media;
			}
		}
		//caso 3: qui, sicuramente, il tot dei crediti è minore al valore numeroCrediti
		if(livello == listEsami.size())
			return;
		
		//dato livello L genera livello L + 1
		
		//caso 0: non aggiungo l'ellesimo Esame al sottoinsieme
		generaTuttiSottoinsiemi(parziale, numeroCrediti, livello + 1);
		//caso 1: aggiungo l'ellesimo esame a parziale
		parziale.add(listEsami.get(livello));
		generaTuttiSottoinsiemi(parziale, numeroCrediti, livello + 1);
			//backtracking
		parziale.remove(listEsami.get(livello));
	}

	private double calcolaMedia(Set<Esame> parziale) {
		int totCrediti = 0;
		double media = 0.0;
		for(Esame e: parziale) {
			totCrediti += e.getCrediti();
			media += e.getVoto() * e.getCrediti();
		}
		return media / totCrediti;
	}

	private int calcolaCrediti(Set<Esame> parziale) {
		int totale = 0;
		for(Esame e: parziale)
			totale += e.getCrediti();
		return totale;
	}

}
