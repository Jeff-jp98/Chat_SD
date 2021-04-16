
package br.com.sd.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import br.com.sd.infra.Cliente;
import br.com.sd.infra.Servidor;
import br.com.sd.infra.TextAreaOutputStream;

public class ClienteView extends JFrame {

	
	private JPanel contentPane;
	private JTextField nick;
	private JTextArea textAreaLogs;
	private JButton btnEntrar;
	private JLabel lblNick;
	private JTextField txtmensagem;
	

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteView frame = new ClienteView();
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					SwingUtilities.updateComponentTreeUI(frame);
					System.setOut(new PrintStream(new TextAreaOutputStream(frame.textAreaLogs)));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClienteView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nick = new JTextField();
		nick.setBounds(146, 11, 199, 20);
		contentPane.add(nick);
		nick.setColumns(10);
		
		lblNick = new JLabel("Nome de Usu\u00E1rio:");
		lblNick.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNick.setBounds(10, 14, 146, 14);
		contentPane.add(lblNick);
		
		btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnEntrar) {
					if(btnEntrar.getText().equals("Entrar")) {
						btnEntrar.setText("Sair");
						Cliente.start(nick.getText());
					}else {
						btnEntrar.setText("Entrar");
						Cliente.stop();
					}
				}
			}
		});
		btnEntrar.setBounds(355, 10, 89, 23);
		contentPane.add(btnEntrar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 434, 277);
		contentPane.add(scrollPane);
		
		textAreaLogs = new JTextArea();
		textAreaLogs.setLineWrap(true);
		scrollPane.setViewportView(textAreaLogs);
		
		txtmensagem = new JTextField();
		txtmensagem.setBounds(10, 330, 335, 22);
		contentPane.add(txtmensagem);
		txtmensagem.setColumns(10);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnEnviar) {
					String mensagem = txtmensagem.getText();
					if(!mensagem.isEmpty()) {
						Cliente.saida.println(mensagem);
						txtmensagem.setText("");
					}
				}
			}
		});
		btnEnviar.setBounds(360, 329, 84, 23);
		contentPane.add(btnEnviar);
	}
}