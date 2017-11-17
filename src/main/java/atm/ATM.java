package atm;


import java.io.IOException;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.Scanner;

import interfaces.OperationsInterface;

public class ATM {

	static OperationsInterface bank;
	static Registry registry;
	static Scanner in = new Scanner(System.in);

	/**
	 * Attempt to parse port to connect to, else defaults to 1099 and tries
	 * connecting Starts CLI interface to log in users and presents options for
	 * interacting with server
	 * 
	 * @param args
	 * @throws NotBoundException
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws NotBoundException, SQLException, IOException, ClassNotFoundException {
		
		
		 InetAddress inet = null;
		 InetAddress inet2 = null;
		 String rmi_adress;

		    inet = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, 101, (byte) 137 });
		    System.out.println("Enviando Ping Request para host: " + inet);
		    System.out.println(inet.isReachable(5000) ? "Resposta: Host online" : "Resposta: Host offline");

		    inet2 = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, 101, (byte) 131 });
		    System.out.println("Enviando Ping Request para host: " + inet2);
		    System.out.println(inet2.isReachable(5000) ? "Resposta: Host online" : "Resposta: Host offline");
		
			try {
				
				
				rmi_adress =  "127.0.0.1";
				
				if (inet.isReachable(5000)) {
					rmi_adress = "192.168.101.137";
					registry = LocateRegistry.getRegistry(rmi_adress, 1099);		
					bank = (OperationsInterface) registry.lookup("BankServer");
				} else if (inet2.isReachable(5000)) {
					rmi_adress =  "192.168.101.131";
					registry = LocateRegistry.getRegistry(rmi_adress, 1099);		
					bank = (OperationsInterface) registry.lookup("BankServer");
				}else if (!inet.isReachable(5000) && !inet2.isReachable(5000)){
					rmi_adress =  "127.0.0.1";
					registry = LocateRegistry.getRegistry(rmi_adress, 1099);		
					bank = (OperationsInterface) registry.lookup("BankServer");
				}
				
				System.out.println("RMI Host " + rmi_adress);
			

			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Algo saiu errado..."); 

			}
		
		    
		if (bank != null);

		boolean loggedIn = false;
		String usuario, senha;
		
		
		do {
			
		System.out.println("\n\n   *** Bem vindo ao AssisBank ***   ");

		System.out.println("\nDidite o usuário e a senha: ");
		System.out.println("\nUsuário: ");
		usuario = in.nextLine();
		System.out.println("Senha: ");
		senha = in.nextLine();

		try {
			if (bank.login(usuario, senha)) {
				loggedIn = true;
				
				System.out.println("Login efetuado com sucesso!");
			} else {
				System.out.println("Usuário e/ou senhas incorretas");
				
			}
	
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Erro: Informações incorretas");
		}

		}while(!loggedIn);
		
		while (loggedIn) { 

			System.out.println("\n\n   *** HOME ***   ");
			System.out.println("\nSeu saldo atual: R$" + bank.getBalance());
			System.out.println("\nDigite uma opção: \n" + "1) Deposito\n" + "2) Saque\n" + "3) Extrato\n" + "4) Sair\n\n" + 
			"[exclusivo funcionarios]\n5) Cadastrar Cliente\n");
			
			int operation = in.nextInt();

			switch (operation) {

			case 1:
				System.out.println("Digite o valor a ser depositado: ");
				Double input = in.nextDouble();
				try {
					System.out.println("Depósito efetivado: R$" + input + "\nNovo Saldo: R$" + bank.deposit((input)));
				} catch (NumberFormatException e) {
					System.out.println("Valor inválido. Tente novamemte.");
					break;
				}
				break;

			case 2:
				System.out.println("Digite o valor a ser sacado: R$");
				input = in.nextDouble();
				try {
					System.out.println(
							"Saque efetuado com sucesso: R$" + input + "\nNovo Saldo: R$" + bank.withdraw((input)));
				} catch (NumberFormatException e) {
					System.out.println("Valor inválido. Tente novamemte");
					break;
				}
				break;

			case 3:
				System.out.println("Extrato: ");
				for (int i = 0; i < bank.retrieveTransactionsHistory().size(); i++) {
					System.out.println(bank.retrieveTransactionsHistory().get(i));
				}
				break;

			case 4:
				System.out.println("Tenha um bom dia!");
				System.exit(0);
				break;
				
			case 5:
				try {
					if (bank.checkFuncionario(usuario, senha)) {

						System.out.println("Digite os dados do cliente: ");
						bank.insertNewAccount();

					} else {
						System.out.println("Somente funcionarios podem efetuar cadastros.");
					}
			
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Erro: Informações incorretas");
				}
				
				break;
				
			default:
				System.out.println("Digito não reconhecido, tente novamente.");
				break;
			}
		}

		System.exit(0);
	}

}
