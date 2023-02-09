package gui.izvestaj;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import entity.RezultatTesta;
import entity.Test;
import net.miginfocom.swing.MigLayout;

public class TestIzvestaj extends JDialog{

	private static final long serialVersionUID = 8400185421651697887L;

	public TestIzvestaj(Test t) {
		setTitle("Test - izveštaj");
		ImageIcon img = new ImageIcon("./img/report.png");
		setIconImage(img.getImage());
		setResizable(false);
		setLayout(new MigLayout("wrap 3", "[]10[]10[]", "[]10[]10[]10[]10[]10[]"));
		
		add(new JLabel("Kurs: "));
		JLabel lblKurs = new JLabel();
		lblKurs.setText(t.getKurs().toString());
		add(lblKurs, "span 2");
		
		add(new JLabel("Datum održavanja: "));
		JLabel lblDatum = new JLabel();
		lblDatum.setText(t.getDatum().toString());
		add(lblDatum, "span 2");
		
		add(new JLabel("Predavač: "));
		JLabel lblP = new JLabel();
		lblP.setText(t.getPredavac().toString());
		add(lblP, "span 2");
		
		add(new JLabel("Učenik"));
		add(new JLabel("Bodovi"));
		add(new JLabel("Ocena"));
		
		add(new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap, span 3");
		
		int deset = 0;
		int devet = 0;
		int osam = 0;
		int sedam = 0;
		int sest = 0;
		int np = 0;
		
		for(RezultatTesta rezultat:t.getRezultati()) {
			JLabel ucenik = new JLabel();
			ucenik.setText(rezultat.getUcenik().toString());
			add(ucenik);
			JLabel bodovi = new JLabel();
			bodovi.setText(String.valueOf(rezultat.getBodovi()));
			add(bodovi);
			JLabel ocena = new JLabel();
			ocena.setText(String.valueOf(rezultat.tabelaOcena()));
			add(ocena);
			if(rezultat.getOcena()==10) 
				deset+=1;
			else if(rezultat.getOcena() == 9)
				devet+=1;
			else if(rezultat.getOcena() == 8)
				osam+=1;
			else if(rezultat.getOcena() == 7)
				sedam +=1;
			else if(rezultat.getOcena() == 6)
				sest+=1;
			else
				np+=1;
		}
		add(new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap, span 3");
		
		add(new JLabel("Ocena"));
		add(new JLabel("Prosek"), "span 2");
		add(new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap, span 3");
		
		int size = t.getRezultati().size();
		double p10 = (deset/size)*100;
		double p9 = (devet/size)*100;
		double p8 = (osam/size)*100;
		double p7 = (sedam/size)*100;
		double p6 = (sest/size)*100;
		double pnp = (np/size)*100;
		
		add(new JLabel("10 - "));
		add(new JLabel(String.valueOf(p10)+"%"), "span 2");
		add(new JLabel("9 - "));
		add(new JLabel(String.valueOf(p9)+"%"), "span 2");
		add(new JLabel("8 - "));
		add(new JLabel(String.valueOf(p8)+"%"), "span 2");
		add(new JLabel("7 - "));
		add(new JLabel(String.valueOf(p7)+"%"), "span 2");
		add(new JLabel("6 - "));
		add(new JLabel(String.valueOf(p6)+"%"), "span 2");
		add(new JLabel("Nije položio - "));
		add(new JLabel(String.valueOf(pnp)+"%"), "span 2");
		
		add(new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap, span 3");
		
		add(new JLabel("Prosečna ocena: "));
		double prosek = t.getProsek();
		add(new JLabel(String.valueOf(prosek)), "span 2");
		
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
}
