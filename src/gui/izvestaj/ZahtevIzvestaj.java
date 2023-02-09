package gui.izvestaj;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import entity.Jezik;
import entity.Zahtev;
import managers.ZahtevManager;
import net.miginfocom.swing.MigLayout;

public class ZahtevIzvestaj extends JFrame{

	private Jezik j;
	private LocalDate start;
	private LocalDate end;
	private ZahtevManager zm;
	
  private static final long serialVersionUID = 5147150523792654822L;
	
  
  public ZahtevIzvestaj(ZahtevManager zm, LocalDate start, LocalDate end, Jezik j) {
	  this.zm = zm;
	  this.start = start;
	  this.end = end;
	  this.j = j;
	  
	  setResizable(false);
	  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  setTitle("Obrađeni zahtevi - Izveštaj");
	  ImageIcon img = new ImageIcon("./img/report.png");
	  setIconImage(img.getImage());
	  setLayout(new MigLayout("wrap 4", "[]10[]10[]10[]", "[]10[]10[]10[]10[]10[]10[]"));
	  
	  List<Zahtev> zahtevi = zm.getZahteviIzvestaj(start, end, j);
	  
	  add(new JLabel("Broj obrađenih zahteva: "), "span 2");
	  add(new JLabel(String.valueOf(zahtevi.size())), "span 2");
	  add(new JLabel(), "span 4");
	  if(zahtevi.size()>0) {
		//ucenik, kurs, stanje, datum
		  add(new JLabel("Učenik"));
		  add(new JLabel("Kurs"));
		  add(new JLabel("Stanje"));
		  add(new JLabel("Datum"));
		  add(new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap, span 4");
	  
		  Map<Jezik, Integer> map = new HashMap<Jezik, Integer>();

		  
		  for(Zahtev z:zahtevi) {
			  add(new JLabel(z.getUcenik().toString()));
			  add(new JLabel(z.getKurs().toString()));
			  add(new JLabel(z.getStanje().toString()));
			  add(new JLabel(z.getDatum().toString()));
			  map.put(z.getKurs().getJezik(), 0);
		  }
		  
		  for(Zahtev z:zahtevi) {
			  int br = map.get(z.getKurs().getJezik())+1;
			  map.put(z.getKurs().getJezik(), br);
		  }
		  
		  Map<Jezik, Integer> sortedMap = this.sort(map);
		  Set<Map.Entry<Jezik, Integer>> set = sortedMap.entrySet();
		  Iterator iter = set.iterator();
		  
		  add(new JLabel(), "span 4");
		  add(new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap, span 4");
		  add(new JLabel("Najpopularniji jezici u odabranom periodu: "), "span 4");
		  
		  int i =1;
		  while(iter.hasNext()) {
			  Map.Entry pair = (Map.Entry)iter.next();
			  add(new JLabel(String.valueOf(i)+". " +pair.getKey().toString()), "span 4");
			  i++;
			  if(i>3);
		  }
		  
		  pack();
	  }else {
		  setSize(220, 80);
	  }
	  setLocationRelativeTo(null);
  }
  
  public <K,V extends Comparable<V>> Map<K,V> sort(final Map<K,V> map){
	  Comparator<K> comp = new Comparator<K>() {

    	@Override
    	public int compare(K o1, K o2) {
    		int compare = map.get(o1).compareTo(map.get(o2));
    		if(compare == 0)
    			return 1;
    		else
    			return compare;
    	}
		  
	 };
	 Map<K,V> sorted = new TreeMap<K,V> (comp);
	 sorted.putAll(map);
	 return sorted;
  }
}
