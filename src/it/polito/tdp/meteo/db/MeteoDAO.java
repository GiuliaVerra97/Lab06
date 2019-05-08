package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {
	
	/**
	 * Ricavo tutti i rilevamenti presenti nel DB
	 * @return lista di {@link Rilevamento}
	 */

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";		//ASC ascendente

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);

			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	
	
	/**
	 * Metodo che mi permette di ricavare una lista di rilevamenti di una determinata località e in un mese scelto
	 * @param mese
	 * @param localita
	 * @return lista di {@link Rilevamento}
	 */
	
	public List<Rilevamento> getAllRilevamentiLocalitaMese(Month mese, String localita) {		//era int mese

			final String sql="SELECT localita, Data, Umidita " + 
					"FROM situazione " + 
					"WHERE localita=? and MONTH(Data)=? " + 		//posso ricavare direttamente il mese della data
					"ORDER BY data ";
			
			List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

			
			try {
				Connection conn = DBConnect.getInstance().getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, localita);
				st.setInt(2, mese.getValue());		//setto ? al mese passato come parametro
				ResultSet rs = st.executeQuery();

				while (rs.next()) {

					Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
					/*Date data=rs.getDate("Data");
					DateFormat formatoData = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
					String s = formatoData.format(data);
					int m=Integer.parseInt(s.substring(3, 5));					
					if(mese==m) {
						rilevamenti.add(r);
					}*/
					
					rilevamenti.add(r);
					
					
				
				}

				conn.close();
				return rilevamenti;

			} catch (SQLException e) {

				e.printStackTrace();
				throw new RuntimeException(e);
			}		
	
	}

	
	
	/**
	 * Metodo che ricava la media dell'umidità di ciascuna località in un mese specifico
	 * @param mese
	 * @param localita
	 * @return valore medio umidità 
	 */
	
	
	public Double getAvgRilevamentiLocalitaMese(Month mese, String localita) { //era int 

		List<Rilevamento> rilevamenti= new LinkedList<Rilevamento>();
		rilevamenti=this.getAllRilevamentiLocalitaMese(mese, localita);
		double media=0.0;
		double somma=0;
		
		for(Rilevamento r: rilevamenti) {
			somma=somma+r.getUmidita();
		}
		
		media=somma/(double)rilevamenti.size();
		
		return media;
	}
	
	
	
	
	
	/**
	 * Metodo che mi fornisce una lista delle città presenti nel DB, tutte diverse tra di loro, non voglio che si ripetano
	 * @return lista {@link Citta}
	 */
	public List<Citta> getAllcitta(){
		List<Citta> cittaLista = new LinkedList<Citta>();
		final String sql = "SELECT DISTINCT localita FROM situazione GROUP BY localita ASC"; 
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
			Citta c = new Citta(rs.getString("localita"));
			cittaLista.add(c);
				
			}
			conn.close();
			return cittaLista; 
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	
	
	

}
