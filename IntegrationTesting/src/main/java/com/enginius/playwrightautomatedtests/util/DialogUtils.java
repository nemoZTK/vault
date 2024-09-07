package com.enginius.playwrightautomatedtests.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DialogUtils {
	Logger logger = LoggerFactory.getLogger(getClass());
	private static final int THREAD_POOL_SIZE = 10;
	private static final Map<String, ImageIcon> imageCache = new HashMap<>();

	public <T> Pair<String, T> DialogSelectPairFromMap(Map<String, T> map) {
		JComboBox<String> comboBox = new JComboBox<>(map.keySet().toArray(new String[0]));
		int selectionResult = JOptionPane.showConfirmDialog(null, comboBox, "Seleziona", JOptionPane.OK_CANCEL_OPTION);

		if (selectionResult == JOptionPane.OK_OPTION) {
			String selectedKey = (String) comboBox.getSelectedItem();
			T selectedValue = map.get(selectedKey);
			return Pair.of(selectedKey, selectedValue);
		} else {

			logger.error("Selezione annullata.");
			return null;
		}
	}

	public String dialogSelectStringFromList(List<String> list) {
		JComboBox<String> comboBox = new JComboBox<>(list.toArray(new String[0]));
		int selectionResult = JOptionPane.showConfirmDialog(null, comboBox, "Seleziona", JOptionPane.OK_CANCEL_OPTION);

		if (selectionResult == JOptionPane.OK_OPTION) {
			return (String) comboBox.getSelectedItem();

		} else {

			logger.error("Selezione annullata.");
			return null;
		}
	}

	public boolean askConfirm(String message) {

		int isStarted = JOptionPane.showConfirmDialog(null, message, "RICHIESTA CONFERMA", JOptionPane.YES_NO_OPTION);
		if (isStarted == JOptionPane.NO_OPTION) {
			logger.debug("hai detto di no");
			return false;
		} else {
			logger.debug("hai confermato di si");
			return true;
		}

	}

	public String test(String[] imageUrls) throws InterruptedException, ExecutionException {
		final String[] selectedImageUrl = { null }; // Array per memorizzare l'URL selezionato

		// Creazione e avvio del frame
		JFrame frame = new JFrame("Image JComboBox Demo");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JComboBox<ImageIcon> comboBox = new JComboBox<>();
		Dimension maxSize = new Dimension();

		// Caricamento delle immagini in parallelo
		ImageIcon[] images = loadImages(imageUrls);
		for (ImageIcon image : images) {
			if (image != null) {
				comboBox.addItem(image);
				Dimension imageSize = new Dimension(image.getIconWidth(), image.getIconHeight());
				if (imageSize.width > maxSize.width)
					maxSize.width = imageSize.width;
				if (imageSize.height > maxSize.height)
					maxSize.height = imageSize.height;
			}
		}

		// Impostazioni della JComboBox
		comboBox.setRenderer(new ImageComboBoxRenderer());
		comboBox.setPreferredSize(new Dimension(maxSize.width + 20, maxSize.height + 20));

		// Bottone di conferma
		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageIcon selectedIcon = (ImageIcon) comboBox.getSelectedItem();
				if (selectedIcon != null) {
					for (int i = 0; i < images.length; i++) {
						if (images[i].equals(selectedIcon)) {
							selectedImageUrl[0] = imageUrls[i];
							break;
						}
					}
				}
				frame.dispose(); // Chiude la finestra
			}
		});

		// Configurazione del pannello e del frame
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(comboBox, BorderLayout.NORTH);
		panel.add(confirmButton, BorderLayout.SOUTH);
		frame.getContentPane().add(panel);

		frame.pack();
		frame.setVisible(true);

		// Attende che la finestra venga chiusa
		while (frame.isVisible()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return selectedImageUrl[0];
	}

	private static ImageIcon loadImage(String urlString) {
		if (imageCache.containsKey(urlString)) {
			return imageCache.get(urlString);
		}

		try {
			URL url = new URL(urlString);
			BufferedImage bufferedImage = ImageIO.read(url);
			if (bufferedImage == null) {
				System.err.println("ImageIO returned null for: " + urlString);
				return null;
			}
			ImageIcon icon = new ImageIcon(bufferedImage);
			Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			ImageIcon scaledIcon = new ImageIcon(img);
			imageCache.put(urlString, scaledIcon);
			return scaledIcon;
		} catch (IOException e) {
			System.err.println("Failed to load image from: " + urlString);
			e.printStackTrace();
			return null;
		}
	}

	private ImageIcon[] loadImages(String[] imageUrls) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		List<Future<ImageIcon>> futures = new ArrayList<>();

		for (String url : imageUrls) {
			futures.add(executor.submit(() -> loadImage(url)));
		}

		ImageIcon[] images = new ImageIcon[imageUrls.length];
		for (int i = 0; i < futures.size(); i++) {
			images[i] = futures.get(i).get();
		}

		executor.shutdown();
		return images;
	}

	// Renderer personalizzato per il JComboBox
	static class ImageComboBoxRenderer extends DefaultListCellRenderer {
		@Override
		public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			if (value instanceof ImageIcon) {
				JLabel label = new JLabel((ImageIcon) value);
				label.setPreferredSize(new Dimension(100, 100));
				return label;
			} else {
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		}
	}
}
