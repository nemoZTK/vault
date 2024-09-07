package com.enginius.playwrightautomatedtests.tests.examples;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.enginius.playwrightautomatedtests.util.DialogUtils;

@SpringBootTest
public class InputDialogExample {
	@Autowired
	DialogUtils dialogUtil;

	public static void main(String[] args) {
		System.out.println("opening input dialog...");
		String result = JOptionPane.showInputDialog("what's your name?");
		System.out.println("got: " + result);
	}

	@Test
	public void test() {
		System.setProperty("java.awt.headless", "false");
	}

	@Test
	public void testComboBox() {
		String[] options = { "Opzione 1", "Opzione 2", "Opzione 3" };

		// Creazione del JComboBox con le opzioni
		JComboBox<String> comboBox = new JComboBox<>(options);

		// Mostra la finestra di dialogo con il JComboBox
		int result = JOptionPane.showConfirmDialog(null, comboBox, "Seleziona un'opzione",
				JOptionPane.OK_CANCEL_OPTION);

		// Gestione dell'input dell'utente
		if (result == JOptionPane.OK_OPTION) {
			String selectedOption = (String) comboBox.getSelectedItem();
			System.out.println("Hai selezionato: " + selectedOption);
		} else {
			System.out.println("Operazione annullata.");
		}
	}
}
