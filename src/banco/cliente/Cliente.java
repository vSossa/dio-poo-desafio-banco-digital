package banco.cliente;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Cliente {
	private final int ID;
	private final int CPF;	
	private String nome;
	
	private Map<TipoDeConta, ArrayList<Conta>> contas;

	int numeroDeContasCorrente;
	final int MAXIMO_DE_CONTAS_CORRENTE = 3;

	int numeroDeContasPoupanca;
	final int MAXIMO_DE_CONTAS_POUPANCA = 1;

	public Cliente(String nome, int cpf, int id) {
		this.nome = nome;
		this.CPF = cpf;
		this.ID = id;

		this.contas = new HashMap<>();
			this.contas.put(TipoDeConta.CORRENTE, new ArrayList<>());
			this.numeroDeContasCorrente = 0;

			this.contas.put(TipoDeConta.POUPANCA, new ArrayList<>());
			this.numeroDeContasPoupanca = 0;
	}

	// getters ////
	public String getNome() { return this.nome; }
	public int getCpf()     { return this.CPF; }
	public int getId()      { return this.ID; }
	public ArrayList<Conta> getContasCorrente() {
		return this.contas.get(TipoDeConta.CORRENTE);
	}
	public ArrayList<Conta> getContasPoupanca() {
		return this.contas.get(TipoDeConta.POUPANCA);
	}
	//// getters

	public int menu(Scanner entrada, final ArrayList<Cliente> clientes) {
		int escolha = -1;
		System.out.println("(1) Entrar");
		System.out.println("(2) Criar");
		System.out.println("(3) Informações");
		System.out.println("(4) Mostrar suas contas");
		System.out.println("(0) Sair");
		System.out.print("Escolha: ");
		try {
			escolha = Integer.parseUnsignedInt(entrada.nextLine());
		} catch(NumberFormatException e) {
			System.out.println();
			System.out.println("ERRO: formato inválido para escolha.");
			System.out.println();
		}
		switch(escolha) {
		case 1: { 
			acessarConta(entrada, clientes);
			break;
		}			

		case 2: {
			criarConta(entrada);
			break;
		}

		case 3: {
			mostrarInfo();
			break;
		}

		case 4: {
			mostrarContas();
			break;
		}
		
		case 0: {
			System.out.printf("%nTchau, %s!%n",
							  this.nome);
			break;
		}

		default: {
			System.out.println();
			System.out.println("Opção inválida.");
			System.out.println();
			break;
		}
		}

		return escolha;
	}	

	private void acessarConta(Scanner entrada, ArrayList<Cliente> outrosClientes) {
		int id = -1;
		int escolha = -1;
		Conta conta = null;
		
		System.out.println();
		System.out.println("========= Entrar");
		if (outrosClientes == null) {
			System.out.println("ERRO: nenhum conta cadastrada.");
			return ;
		}

		System.out.print("Identificador: ");
		try {
			id = Integer.parseUnsignedInt(entrada.nextLine());
		} catch(NumberFormatException e) {
			System.out.println();
			System.out.println("ERRO: formato inválido para Identificador.");
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
		}	

		if (escolha == 1) {
			conta = acessarConta(id, TipoDeConta.CORRENTE);	
		} else if (escolha == 2){ 
			conta = acessarConta(id, TipoDeConta.POUPANCA);
		} else {
			System.out.printf("ERRO: esperado '1' ou '2', mas temos: %d%n",
							  escolha);
			return ;
		}

		if ( conta == null ) {
			System.out.println();
			System.out.printf("ERRO: nenhum conta encontrada com id: '%d'.%n", id);
			System.out.println();
		} else {
			operarConta(conta, outrosClientes, entrada);
		}
	}

	private void operarConta(Conta conta, ArrayList<Cliente> outrosClientes, Scanner entrada) {
			System.out.println();
			System.out.println("========= Conta");
			while (conta.menu(entrada, outrosClientes) != 0);
	}
	
	private Conta acessarConta(int id, TipoDeConta t) {
		if ( this.contas.get(t).isEmpty() ) return null;	
		
		for (Conta conta : this.contas.get(t)) {
			if (conta == null) break;
			if (conta.getId() == id) return conta;;
		}

		return null;
	}

	private void criarConta(Scanner entrada) {
		TipoDeConta t = null;
		int agencia = -1;

		int escolha = -1;	

		System.out.println();
		System.out.println("========= Criar conta");
		System.out.println("(1) Corrente");
		System.out.println("(2) Poupança");
		System.out.print("Escolha: ");
		try {
			escolha = Integer.parseUnsignedInt(entrada.nextLine());	
		} catch(NumberFormatException e) {
			System.out.println();
			System.out.println("ERRO: formato inválido para Escolha.");
			System.out.println();
		}
		if (escolha == 1) { // corrente
			final int id = this.numeroDeContasCorrente + 1;
			if (id > this.MAXIMO_DE_CONTAS_CORRENTE) {
				System.out.println();
				System.out.println("ERRO: limite de contas corrente já foi alcançado"); 
				System.out.println();
				return ;
			}
			t = TipoDeConta.CORRENTE;
			
			System.out.println();
			System.out.print("Agência: ");
			agencia = Integer.parseUnsignedInt(entrada.nextLine());
			this.numeroDeContasCorrente = id;
			this.contas.get(t).add( new ContaCorrente(id, agencia) );
			System.out.println("Conta criada com sucesso!");
			System.out.println();
		} else if (escolha == 2) { // poupanca
			final int id = this.numeroDeContasPoupanca + 1;
			if (id > this.MAXIMO_DE_CONTAS_POUPANCA) {
				System.out.println();
				System.out.println("ERRO: limite de contas poupança já foi alcançado"); 
				System.out.println();
				return ;
			}
			t = TipoDeConta.POUPANCA;
			
			System.out.println();
			System.out.print("Agência: ");
			agencia = Integer.parseUnsignedInt(entrada.nextLine());
			this.numeroDeContasPoupanca = id;
			this.contas.get(t).add( new ContaPoupanca(id, agencia) );
			System.out.println("Conta criada com sucesso!");
			System.out.println();
		} else {
			System.out.println();
			System.out.printf("ERRO: esperado '1' ou '2', mas temos '%d'.%n", 
							   escolha);
			System.out.println();
		}
	}
	
	private void mostrarInfo() {
		System.out.println();	
		System.out.println("========= Info");	
		System.out.println("Nome: " + this.nome);
		System.out.println("CPF: " + this.CPF);
		System.out.println("Id: " + this.ID);
		System.out.println();	
	}

	private void mostrarContas() {
		System.out.println();
		System.out.println("========= Contas");
		if (this.contas.get(TipoDeConta.CORRENTE).isEmpty() &&
			this.contas.get(TipoDeConta.POUPANCA).isEmpty()) { 
			System.out.println("Nenhuma conta cadastrada.");
			System.out.println();
			return ;
		}

		for (TipoDeConta t : TipoDeConta.values()) {
			ArrayList<Conta> contas = this.contas.get(t);
			if (contas == null) continue;
			for (Conta conta : contas) {
				System.out.println("" + conta);
				System.out.println();
			} 
		}
		System.out.println();
	}

	@Override
	public String toString() {
		return String.format(
			"Nome: %s%nCPF: %d%nIdentificador: %d%n", 
			this.nome, this.CPF, this.ID
		);
	}
}
