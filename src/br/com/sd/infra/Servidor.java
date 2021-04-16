package br.com.sd.infra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;


public class Servidor {
	//variaveis
	public static SimpleDateFormat formato = new SimpleDateFormat("[hh:mm a]");
	private static HashMap<String, PrintWriter>clientesConectados = new HashMap<String, PrintWriter>();
	private static ServerSocket servidor;
	private static volatile boolean exit = false;
	
	//metodos
	
	
	
	public static void start() {
		
		new Thread(new ServerHandler()).start();
	}
	public static void stop()throws IOException{
		
		if(!servidor.isClosed()) {
			servidor.close();
		}
	}
	public static void broadcast(String mensagem) {
		
		for(PrintWriter p: clientesConectados.values()) {
			p.println(mensagem);
		}
	}
	public static void addToLogs(String mensagem) {
		
		System.out.printf("%s %s\n",formato.format(new Date()),mensagem);
	}
	private static class ServerHandler implements Runnable{

		@Override
		public void run() {
			
			try {
				servidor=new ServerSocket(12345);
				addToLogs("Servidor iniciou");
				
				addToLogs("Aguardando Conexoes");
				while(!exit) {
					new Thread(new ClienteHandler(servidor.accept())).start();
				}
			}
			catch (Exception e) {
				addToLogs("Erro");
				addToLogs(Arrays.toString(e.getStackTrace()));
			}
			
		}
		
	}
	private static class ClienteHandler implements Runnable{
		private Socket cliente;
		private PrintWriter saida;
		private BufferedReader entrada;
		private String nome;
		
		public ClienteHandler(Socket cliente) {
			this.cliente=cliente;
		}

		@Override
		public void run() {
			
			addToLogs("Cliente Conectado: " + cliente.getInetAddress());
			try {
				entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
				saida = new PrintWriter(cliente.getOutputStream(),true);
				for(;;) {
					nome = entrada.readLine();
					if (nome == null) {
						return;
					}
					synchronized (clientesConectados) {
						if (!nome.isEmpty() && !clientesConectados.keySet().contains(nome)) { 
							break;
						}
						else {
							saida.println("INVALIDNAME");
						}
					}
				}
				saida.println("Bem vindo ao chat: "+nome);
				addToLogs(nome +" se juntou a conversa");
				broadcast("[SYSTEM] " +nome+ " se juntou ao chat");
				clientesConectados.put(nome, saida);
				String mensagem;
				saida.println("Voce se juntou ao chat");
				while((mensagem=entrada.readLine())!=null && !exit) {
					if(!mensagem.isEmpty()) {
						if(mensagem.toLowerCase().equals("/sair")) break;
						broadcast(String.format("[%s] %s", nome, mensagem));
					}
				}
			}
			catch (Exception e) {
				addToLogs(e.getMessage());
			}
			finally {
				if(nome != null) {
					addToLogs(nome + " esta saindo");
					clientesConectados.remove(nome);
					broadcast(nome + " saiu");
				}
			}
			
		}
	}
}
