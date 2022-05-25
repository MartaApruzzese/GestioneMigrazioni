package it.polito.tdp.borders.model;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import java.util.*;

public class Simulazione {

	//Coda degli eventi
	private PriorityQueue<Event> queue;
	
	//Paramentri di simulazione
	private int nInizialiMigranti;
	private Country nazioneIniziale;
	
	
	//Output di simulazione
	private int nPassi;
	private Map<Country, Integer> persone; //Migranti stanziati in quella nazione
	
	
	//Stato del mondo simulato
	private Graph<Country, DefaultEdge> grafo;


	public Simulazione(Graph<Country, DefaultEdge> grafo) {
		super();
		this.grafo = grafo;
	}
	
	public void init(Country partenza, int migranti) {
		this.nazioneIniziale=partenza;
		this.nInizialiMigranti=migranti;
		this.queue= new PriorityQueue<>();
		
		this.persone= new HashMap<Country, Integer>();
		for (Country c: this.grafo.vertexSet()){
			this.persone.put(c, 0);
		}
		
		this.queue.add(new Event(1, this.nazioneIniziale, this.nInizialiMigranti));
		
	}


	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e= this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		int stanziali= e.getPersone()/2;
		int migranti= e.getPersone()-stanziali;
		
		//Voglio sapere quanti stati confinanti ho
		int confinanti= this.grafo.degreeOf(e.getNazione());
		int gruppiMigranti= migranti/confinanti;
		stanziali+= migranti%confinanti; // aggiungo il resto della divisione
	
		
		this.persone.put(e.getNazione(), this.persone.get(e.getNazione())+stanziali);
		this.nPassi= e.getTime();
		if(gruppiMigranti!=0) {
			for(Country vicino : Graphs.neighborListOf(this.grafo, e.getNazione())){
				this.queue.add(new Event(e.getTime()+1, vicino, gruppiMigranti));
			}	
		
		}
	}

	public int getnPassi() {
		return nPassi;
	}

	public Map<Country, Integer> getPersone() {
		return persone;
	}
	
	
}
