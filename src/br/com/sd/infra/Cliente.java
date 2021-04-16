package br.com.sd.infra;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Date;

import javax.swing.JOptionPane;

public class Cliente {
	private static Socket clienteSocket;
	public static PrintWriter saida;
	private static String clienteNome;
	
	public static void start(String nick) {
		try {
			clienteNome = nick;
			clienteSocket = new Socket("127.0.0.1", 12345);
			saida = new PrintWriter(clienteSocket.getOutputStream(), true);
			new Thread(new Listener()).start();
			saida.println(clienteNome);
		}
		catch (ConnectException e) {
			JOptionPane.showMessageDialog(null, "Servidor inativo\nEncerre a aplicacao e inicie o servidor para acessar este recurso", "SERVIDOR INATIVO", JOptionPane.ERROR_MESSAGE);
			addToLogs("[ERRO] "+e.getLocalizedMessage());
			System.exit(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			addToLogs("[ERRO] "+e.getLocalizedMessage());
		}
	}
	public static void stop() {
		if(!clienteSocket.isClosed()) {
			try {
				clienteSocket.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void addToLogs(String mensagem) {
		System.out.printf("%s %s\n", Servidor.formato.format(new Date()),mensagem);
	}
	private static class Listener implements Runnable{
		private BufferedReader entrada;

		@Override
		public void run() {
			try {
				entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
				String read;
				for(;;) {
					read=entrada.readLine();
					if (read != null && !(read.isEmpty())) addToLogs(read);
				}
			}catch (Exception e) {
				e.printStackTrace();
				return;
			}	
		}
	}


}
