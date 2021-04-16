package br.com.sd.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import br.com.sd.infra.Servidor;
import br.com.sd.infra.TextAreaOutputStream;

public class ServerView extends JFrame{

	private static volatile boolean exit = false;
	
	private JPanel contentPane;
	private JTextArea textAreaLogs;
	private JButton btnIniciar;
	private JLabel lblServidor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerView frame = new ServerView();
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					SwingUtilities.updateComponentTreeUI(frame);
					//Logs
					System.setOut(new PrintStream(new TextAreaOutputStream(frame.textAreaLogs)));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblServidor = new JLabel("Servidor");
		lblServidor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblServidor.setBounds(5, 5, 444, 36);
		lblServidor.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblServidor);
		
		btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==btnIniciar) {
					if(btnIniciar.getText().equals("Iniciar")) {
						exit =false;
						Servidor.start();
						btnIniciar.setText("Parar");
					}
					else {
						Servidor.addToLogs("Servidor parou");
						try {
							Servidor.stop();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						exit=true;
						btnIniciar.setText("Iniciar");
					}
				}
			}
		});
		btnIniciar.setBounds(315, 13, 89, 23);
		contentPane.add(btnIniciar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 42, 444, 314);
		contentPane.add(scrollPane);

		textAreaLogs = new JTextArea();
		textAreaLogs.setLineWrap(true);		
		scrollPane.setViewportView(textAreaLogs);
	}
}
