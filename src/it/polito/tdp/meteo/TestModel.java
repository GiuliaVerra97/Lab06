package it.polito.tdp.meteo;

import java.time.Month;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		System.out.println(m.getUmiditaMedia(Month.DECEMBER));
		
		System.out.println(m.calcolaSequenza(Month.JANUARY));
		
		System.out.println(m.calcolaSequenza(Month.APRIL));
	}

}
