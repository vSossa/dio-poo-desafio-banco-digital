package banco.cliente;

import java.util.Scanner;
import java.util.ArrayList;

public abstract class Conta { 
	private int ID;
	private int agencia;
	private double saldo;	

	public Conta(int id, int agencia) {
		this.ID = id;
		this.agencia = agencia;

		this.saldo = 0.0;	
	}

	// getters ////
	public int getId()	 { return this.ID; }
	public int getAgencia()	 { return this.agencia; }
	public double getSaldo() { return this.saldo; }
	//// getters

	public int menu(Scanner entrada, ArrayList<Cliente> outrosClientes) {
		int escolha = -1;
		System.out.println("(1) Mostrar saldo");
		System.out.println("(2) Saque");
		System.out.println("(3) Depositar");
		System.out.println("(4) Transferir");
		System.out.println("(5) Informações");
		System.out.println("(0) Sair");
		System.out.print("Escolha: ");
		try {
			escolha = Integer.parseUnsignedInt(entrada.nextLine());
		} catch(NumberFormatException e) {
			System.out.println();
			System.out.println("ERRO: formato inválido para Escolha.");
			System.out.println();
		}
		switch(escolha) {
		case 1: { // ver saldo
			mostrarSaldo();				
			break;
		} 
	
		case 2: { // sacar
			fazerSaque(entrada);	
			break;
		}

		case 3: { // depositar 
			fazerDeposito(entrada);
			break;
		}

		case 4: { // transferir 
			fazerTransferencia(entrada, outrosClientes);
			break;
		}

		case 5: { // mostrar info
			mostrarInfo();
			break;
		}

		case 0: { // sair
			System.out.println();
			System.out.println("Tchau.");
			break;
		}

		default: { // invalido
			System.out.println();
			System.out.println("Opção inválida.");
			break;
		}
		}
	
		return escolha;
	}

	private void mostrarSaldo() {
		System.out.println();
		System.out.println("========== Saldo");
		System.out.printf("Saldo: %.2f.%n", this.saldo);	
		System.out.println();
	}

	private void fazerSaque(Scanner entrada) {
		double saque = -1;

		System.out.println();
		System.out.println("========= Saque");
		System.out.print("Valor do saque: ");	
		try {
			saque = Double.parseDouble(entrada.nextLine());
		} catch(NumberFormatException e) {
			System.out.println();
			System.out.println("ERRO: formato inválido para Saque.");
			System.out.println();
		}
		if (saque < 0) {
			System.out.println();
			System.out.printf("ERRO: espero valor não negativo, mas temos: %.2f.%n", 
							  saque);
			System.out.println();
		} else if (saque > this.saldo) {
			System.out.println();
			System.out.println("ERRO: saldo insuficiente.");
			System.out.println();
		} else {
			System.out.println();
			sacar(saque);	
			System.out.println("Saque bem sucedido!");
			System.out.println();
		}
	}
	
	private void sacar(double saque) {
		this.saldo -= saque;
	}

	private void fazerDeposito(Scanner entrada) {
		double deposito = -1;
		
		System.out.println();
		System.out.println("========= Depositar");
		System.out.print("Valor do depósito: ");
		deposito = Double.parseDouble(entrada.nextLine());
	
		if (deposito < 0) {
			System.out.println();
			System.out.printf("ERRO: espero valor não negativo, mas temos: %.2f.%n", 
							  deposito);
			System.out.println();
		} else {
			System.out.println();
			depositar(deposito);
			System.out.println("Depósito bem sucedido.");
			System.out.println();
		}
	}

	private void depositar(double deposito) {
		this.saldo += deposito;
	}

	private void fazerTransferencia(Scanner entrada, ArrayList<Cliente> outrosClientes) {
		double transferencia = -1;
		int idDestinatario = -1;
		Conta conta = null;

		System.out.println();
		System.out.println("========= Transferência");
		System.out.print("Valor para transferir: ");
		try {
			transferencia = Double.parseDouble(entrada.nextLine());
		} catch(NumberFormatException e) {
			System.out.println();
			System.out.println("ERRO: formato inválido para Transferência.");
			System.out.println();
		}
		if (transferencia < 0) {
			System.out.println();
			System.out.printf("ERRO: espero valor não negativo, mas temos: %.2f.%n", 
							  transferencia);
			System.out.println();
			return ;
		}
		if (transferencia > this.saldo) {
			System.out.println();
			System.out.println("ERRO: saldo insuficiente.");
			System.out.println();
			return ;
		}  
	
		System.out.print("Identificador da conta do destinatário: ");
		try {	
			idDestinatario = Integer.parseUnsignedInt(entrada.nextLine());	
		} catch(NumberFormatException e) {
			System.out.println();	
			System.out.println("ERRO: formato inválido para Id.");
			System.out.println();	
		}

		conta = buscarContaDestinatario(idDestinatario, outrosClientes);	
		if (conta == null) {
			System.out.println();
			System.out.printf("ERRO: nenhuma conta com Id: '%d'.%n",
							  idDestinatario);
			System.out.println();
		} else {
			System.out.println();
			conta.depositar(transferencia);
			this.sacar(transferencia);	
			System.out.println("Tranferência bem sucedida!");
			System.out.println();
		}
	}

	private Conta buscarContaDestinatario(int id, ArrayList<Cliente> outrosClientes) {
		for (Cliente cliente : outrosClientes) {
			if (cliente == null) return null;	
			for (Conta conta : cliente.getContasCorrente()) { 
				if (conta == null) break;
				if (conta.getId() == id) return conta;
			}		
		}

		return null;
	}

	private void mostrarInfo() {
		System.out.println();
		System.out.println("========= Info");
		System.out.println("Id: " + this.ID);
		System.out.println("Agência: " + this.agencia);
		System.out.printf("Saldo: %.2f%n", this.saldo);
		System.out.println();
	}
}
