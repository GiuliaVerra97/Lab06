package it.polito.tdp.meteo;

import java.net.URL;
import java.time.Month;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Citta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Month> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	private Model model;

	/**
	 * Metodo che usa la ricorsione
	 * @param event
	 */
	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		txtResult.clear();
		if(boxMese.getValue()!=null) {
			List<Citta> sequenza=model.calcolaSequenza(boxMese.getValue());
			for(Citta c: sequenza) {
			txtResult.appendText(c.getNome()+"\n");
			}
		}
	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		txtResult.clear();
		if(boxMese.getValue()!=null) {
			String s=model.getUmiditaMedia(boxMese.getValue());
			txtResult.appendText(s);
		}
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
	}

	public void setModel(Model model) {
		this.model=model;
		for(int i=1; i<13;i++) {
			boxMese.getItems().add(Month.of(i));		//implementazione tendina
		}
		
	}
	
	
	
	
	
	

}
