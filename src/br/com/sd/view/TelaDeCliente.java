package br.com.sd.view;

import java.awt.Button;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import br.com.sd.infra.Cliente;
import br.com.sd.infra.Servidor;
import br.com.sd.infra.TextAreaOutputStream;

public class TelaDeCliente extends JFrame {

	
	private JPanel contentPane;
	private JTextField nick;
	private JTextArea textAreaLogs;
	private JButton btnEntrar;
	private JLabel lblNick;
	private JTextField txtmensagem;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaDeCliente frame = new TelaDeCliente();
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

	/**
	 * Create the frame.
	 */
	public TelaDeCliente() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(dimensoesDaTela());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nick = new JTextField();
		nick.setBounds(213, 12, 247, 29);
		contentPane.add(nick);
		nick.setColumns(10);
		
		lblNick = new JLabel("Nome de Usuario:");
		lblNick.setFont(new Font("Menlo", Font.PLAIN, 18));
		lblNick.setBounds(10, 12, 191, 29);
		contentPane.add(lblNick);
		
		btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					if(e.getSource() == btnEntrar) {

						if(btnEntrar.getText().equals("Entrar")) {
							btnEntrar.setText("Sair");
							Cliente.start(nick.getText());
						}
						else {
							btnEntrar.setText("Entrar");
							Cliente.stop();
						}
					}
			}
		});
		btnEntrar.setBounds(472, 6, 89, 35);
		contentPane.add(btnEntrar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 57, 624, 495);
		contentPane.add(scrollPane);
		
		textAreaLogs = new JTextArea();
		textAreaLogs.setLineWrap(true);
		scrollPane.setViewportView(textAreaLogs);
		
		txtmensagem = new JTextField();
		txtmensagem.setBounds(10, 564, 492, 48);
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
		btnEnviar.setBounds(514, 564, 120, 48);
		contentPane.add(btnEnviar);
	}
	
	public static final Rectangle dimensoesDaTela() {

		Integer minX = 0;
		Integer minY = 0;
		Integer maxX = 0;
		Integer maxY = 0;
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		for(GraphicsDevice device : environment.getScreenDevices()){
			Rectangle bounds = device.getDefaultConfiguration().getBounds();
			minX = Math.min(minX, bounds.x);
			minY = Math.min(minY, bounds.y);
			maxX = Math.max(maxX,  bounds.x+bounds.width);
			maxY = Math.max(maxX, bounds.y+bounds.height);
		}
		
		return new Rectangle(minX, minY, (maxX - minX)/2, (maxY - minY)/2);
	}
}
