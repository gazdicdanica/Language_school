package gui.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import entity.Kurs;
import entity.Predavac;
import entity.Test;
import managers.TestManager;
import net.miginfocom.swing.MigLayout;

public class TestAddEdit extends JDialog{
	private TestManager tm;
	private Test test;
	private Kurs k;
	private Predavac p;
	
	private static final long serialVersionUID = 4267086125452589887L;

	public TestAddEdit(TestManager tm, Test test, Kurs k, Predavac p) {
		
		this.test = test;
		this.tm = tm;
		this.k = k;
		this.p = p;
		
		setModal(true);
		if(test!=null) {
			setTitle("Izmena datuma testa.");
		}else
			setTitle("Dodavanje testa.");
		setResizable(false);
		setLayout(new MigLayout("wrap 2", "[]10[]", "[]10[]"));
		add(new JLabel("Unesite datum: "));
		JTextField tf = new JTextField(15);
		add(tf);
		JButton btnSacuvaj = new JButton("Sačuvaj");
		add(btnSacuvaj, "cell 1 1");
		
		btnSacuvaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					LocalDate datum = LocalDate.parse(tf.getText().trim());
					if(datum.compareTo(LocalDate.now())>0) {
						if(test==null) {
							tm.dodajTest(k, datum, p);
							TestAddEdit.this.dispose();
						}else {
							tm.izmeniTest(test, datum);
							TestAddEdit.this.dispose();
						}
					}else {
						JOptionPane.showMessageDialog(null, "Uneli ste datum koji je već prošao.", "Greška!", JOptionPane.WARNING_MESSAGE);
					}
				}catch(DateTimeParseException d) {
					JOptionPane.showMessageDialog(null, "Unesite datum oblika gggg-mm-dd!", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		});
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
}
