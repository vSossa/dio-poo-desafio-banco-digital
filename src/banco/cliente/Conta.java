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
		System.out.print("Escolha: ");
		escolha = Integer.parseUnsignedInt(entrada.nextLine());

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

		case 5: { // mostrar contas
			mostrarContas();
			break;
		}

		case 0: { // sair
			System.out.println("Tchau.");
			break;
		}

		default: { // invalido
			System.out.println("Opção inválida.");
			break;
		}
		}
	
		return escolha;
	}

	private void mostrarSaldo() {
		System.out.printf("Saldo: %.2f.%n", this.saldo);	
	}

	private void fazerSaque(Scanner entrada) {
		double saque = -1;

		System.out.print("Valor do saque: ");	
		saque = Double.parseDouble(entrada.nextLine());
		if (saque < 0) {
			System.out.printf("ERRO: espero valor não negativo, mas temos: %.2f.%n", 
							  saque);
		} else if (saque > this.saldo) {
			System.out.println("ERRO: saldo insuficiente.");
		} else {
			sacar(saque);	
		}
	}
	
	private void sacar(double saque) {
		this.saldo -= saque;
	}

	private void fazerDeposito(Scanner entrada) {
		double deposito = -1;
		
		System.out.print("Valor do depósito: ");
		deposito = Double.parseDouble(entrada.nextLine());
	
		if (deposito < 0) {
			System.out.printf("ERRO: espero valor não negativo, mas temos: %.2f.%n", 
							  deposito);
		} else {
			depositar(deposito);
		}
	}

	private void depositar(double deposito) {
		this.saldo += deposito;
	}

	private void fazerTransferencia(Scanner entrada, ArrayList<Cliente> outrosClientes) {
		double transferencia = -1;
		int idDestinatario = -1;
		Conta conta = null;

		System.out.print("Valor para transferir: ");
		transferencia = Double.parseDouble(entrada.nextLine());
		if (transferencia < 0) {
			System.out.printf("ERRO: espero valor não negativo, mas temos: %.2f.%n", 
							  transferencia);
			return ;
		}
		if (transferencia > this.saldo) {
			System.out.println("ERRO: saldo insuficiente.");
			return ;
		}  
	
		System.out.print("Identificador da conta do destinatário: ");
		idDestinatario = Integer.parseUnsignedInt(entrada.nextLine());	
		conta = buscarContaDestinatario(idDestinatario, outrosClientes);	
		if (conta == null) {
			System.out.printf("ERRO: nenhuma conta com Id: '%d'.%n",
							  idDestinatario);
		} else {
			conta.depositar(transferencia);
			this.sacar(transferencia);	
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

	private void mostrarContas() {
		return ;
	}
}
