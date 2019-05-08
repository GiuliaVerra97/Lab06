package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private MeteoDAO dao=new MeteoDAO();
		
	List<Citta> leCitta;
	List<Citta> listBest;
	double costo_best=Double.MAX_VALUE;		//assegno il massimo valore
	
	
	
	public Model() {
		leCitta=dao.getAllcitta();
	}

	/**
	 * Metodo che mi permette di ricavare sotto forma di stringa l'umidità di ciascuna località presente nel DB, 
	 * in un mese scelto dall'utente
	 * @param mese della classe {@link Month}
	 * @return stringa che registra l'umidità media di ciascuna località scelta
	 */
	
	
	public String getUmiditaMedia(Month mese) {		//era int e lo modifico in Month

		List<String> listaLocalita=new LinkedList<String>();
		double media=0.0;
		String s="";
		
		for(Rilevamento r: dao.getAllRilevamenti()) {
			if(listaLocalita.contains(r.getLocalita())==false) {
				listaLocalita.add(r.getLocalita());
			}
		}
		
		
		for(String localita: listaLocalita) {
			media=dao.getAvgRilevamentiLocalitaMese(mese, localita);
			s=s+" "+localita+" "+media+"\n";
		}
		
		
		return s;
	}
	
	
	
	
	//RICORSIONE.......................................
	


	/**
	 * Metodo pubblico che fa riferimento alla ricorsione
	 * @param mese
	 * @return lista di {@link citta} che deve essere monitorata nel mese indicato
	 */
	public List<Citta> calcolaSequenza(Month mese){
		
		this.listBest=null;
		List<Citta> parziale=new ArrayList<>();
		
		for (Citta c : leCitta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));		//setto tutti i rilevamenti di un mese in una città nella lista rilevamenti di quella città
		}
		
		cerca(parziale, 0);
		return listBest;
	}
	
	
	/**
	 * Metodo RICORSIONE. Valuta quale sia la sequenza migliore di città da monitorare per rendere minimo il costo
	 * @param parziale, ovvero una lista di città
	 * @param livello che aumenta a ogni richiamo della ricorsione
	 */
	private void cerca(List<Citta> parziale, int livello) {
		
		if(parziale.size()==NUMERO_GIORNI_TOTALI) {	//se il num dei rilevamenti presenti nel parziale=15 giorni
			double 	costo=calcolaCosto(parziale);		//calcolo il costo totale della sequenza
				if(costo<costo_best || listBest==null) {		//nel caso in cui non ho ancora inserito nulla nella lista
					costo_best=costo;
					listBest=new ArrayList<>(parziale);
					return;
				}
		}
		
		
		for(Citta c:leCitta) {
			if(controllaParziale(c, parziale)) {		//se sono rispettati i vincoli
				parziale.add(c);
				cerca(parziale, livello+1);
				parziale.remove(parziale.size()-1);		//posso metterlo anche fuori dall' if, ma dentro il for
			}
		}
		
		
	}
	
	


	
	/**
	 * Controlla se sono stati rispettati tutti i vincoli
	 * @param parziale
	 * @return true o false
	 */
	private boolean controllaParziale(Citta prova, List<Citta> parziale) {
		
		//conta quante volte il controllo dell'umidità viene effettuato di seguito nella stessa città 
		int conta=0;
		for(Citta precedente: parziale) {
			if(precedente.equals(prova)) {
				conta++;
			}
		}
		
		//verifica che i giorni massimi consegutivi in una stessa città non siano più di 6
		if(conta>=NUMERO_GIORNI_CITTA_MAX) {	
			return false;
		}
		
		if(parziale.size()==0) {
			return true;
		}
		
		if(parziale.size()==1 || parziale.size()==2) {	
			return parziale.get(parziale.size()-1).equals(prova);		//ritorna true se uguali, false se diversi
		}	
		
		if(parziale.get(parziale.size()-1).equals(prova)) {
			return true;
		}
	
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3))){
			return true;		//se adesso la città è diversa me prima c'erano almeno 3 citta uguali consecutive
		}
		return false;
	}
	
	
	

	
	private Double calcolaCosto(List<Citta> parziale) {
		double costo=0.0;
		//dove mi trovo
		for(int giorno=1; giorno<NUMERO_GIORNI_TOTALI; giorno++) {		//era giorno=1 e giorno<=NUM
			Citta c=parziale.get(giorno);		//era giorno-1, anche sotto
			double umidita=c.getRilevamenti().get(giorno).getUmidita();	//che umidita ho in quella data citta  e in quel dato giorno
			costo=costo+umidita;
		}
		//se si trasferisce
		for(int giorno=1; giorno<NUMERO_GIORNI_TOTALI; giorno++) {		//era 2, <=NUM
			if(!parziale.get(giorno-1).equals(parziale.get(giorno))) {		//era -1 e -2
				costo=costo+COST;
			}
		}
		return costo;	
	}
	
	

}
