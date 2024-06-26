package banco.cliente;

import java.util.Scanner;
import java.util.ArrayList;

public abstract class Conta { 
	protected int ID;
	protected int agencia;
	protected double saldo;	

	public Conta(int id, int agencia) {
		this.ID = id;
		this.agencia = agencia;

		this.saldo = 0.0;	
	}

	// getters ////
	public int getId()	     { return this.ID; }
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
		case 1: {
			mostrarSaldo();				
			break;
		} 
	
		case 2: {
			fazerSaque(entrada);	
			break;
		}

		case 3: {
			fazerDeposito(entrada);
			break;
		}

		case 4: {
			fazerTransferencia(entrada, outrosClientes);
			break;
		}

		case 5: {
			mostrarInfo();
			break;
		}

		case 0: {
			System.out.println();
			System.out.println("Tchau.");
			break;
		}

		default: {
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
		int cpfDestinatario = -1;
		int idDestinatario = -1;
		int escolha = -1;
		Cliente cliente = null;
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
			return ;
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

		System.out.print("CPF do destinatário: ");
		try {	
			cpfDestinatario = Integer.parseUnsignedInt(entrada.nextLine());	
		} catch(NumberFormatException e) {
			System.out.println();	
			System.out.println("ERRO: formato inválido para CPF.");
			System.out.println();	
			return ;
		}
		cliente = buscarCliente(cpfDestinatario, outrosClientes);

		if (cliente == null) {
			System.out.println();	
			System.out.printf("ERRO: '%d' não encontrado.%n", cpfDestinatario);
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
			return ;
		}
		if (idDestinatario == this.ID) {
			System.out.println();
			System.out.println("ERRO: não é possível transferir para si mesmo.");
			System.out.println();
		}

		System.out.println();	
		System.out.println("Tipo de conta");
		System.out.println("(1) Corrente");
		System.out.println("(2) Poupança");
		System.out.print("Escolha: ");
		try {
			escolha = Integer.parseUnsignedInt(entrada.nextLine());
		} catch(NumberFormatException e) {
			System.out.println();
			System.out.println("ERRO: formato inválido para Escolha.");
			System.out.println();
			return ;
		}	
		if (escolha == 1) {
			conta = buscarConta(cliente, idDestinatario, TipoDeConta.CORRENTE);	
		} else if (escolha == 2) { 
			conta = buscarConta(cliente, idDestinatario, TipoDeConta.POUPANCA);	
		} else {
			System.out.printf("ERRO: esperado '1' ou '2', mas temos: %d%n",
							  escolha);
			return ;
		}

		if (conta == null) {
			System.out.println();
			System.out.printf("ERRO: nenhuma conta com Id: '%d', para o CPF: '%d'.%n",
							  idDestinatario, cpfDestinatario);
			System.out.println();
		} else {
			System.out.println();
			conta.depositar(transferencia);
			this.sacar(transferencia);	
			System.out.println("Transferência bem sucedida!");
			System.out.println();
		}
	}

	private Cliente buscarCliente(int cpf, ArrayList<Cliente> clientes) {
		for (Cliente cliente : clientes) {
			if (cliente.getCpf() == cpf) return cliente;
		}	

		return null;
	}

	private Conta buscarConta(Cliente cliente, int id, TipoDeConta t) {
		if (t == TipoDeConta.CORRENTE) {
			for (Conta conta : cliente.getContasCorrente()) {
				if (conta.getId() == id) return conta;
			}
		} else { 
			for (Conta conta : cliente.getContasPoupanca()) {
				if (conta.getId() == id) return conta;
			}
		} 

		return null;
	}

	private void mostrarInfo() {
		System.out.println();
		System.out.println("========= Info");
		System.out.println("" + this);
		System.out.println();
	}
}
