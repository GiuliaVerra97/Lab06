package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private MeteoDAO dao=new MeteoDAO();
		
	
	
	
	
	public Model() {

	}

	/**
	 * Metodo che mi permette di ricavare sotto forma di stringa l'umidità di ciascuna località presente nel DB, 
	 * in un mese scelto dall'utente
	 * @param mese della classe {@link Month}
	 * @return stringa che registra l'umidità media di ciascuna località
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
	
	
	
	
	
	
	

	public String trovaSequenza(int mese) {
		return "TODO!";
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}
	
	
	

	
	
	
	

}
