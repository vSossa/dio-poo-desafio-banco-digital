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

	int numeroDeContasCorrente = 0;
	final int MAXIMO_DE_CONTAS_CORRENTE = 3;

	int numeroDeContasPoupanca = 0;
	final int MAXIMO_DE_CONTAS_POUPANCA = 1;

	public Cliente(String nome, int cpf, int id) {
		this.nome = nome;
		this.CPF = cpf;
		this.ID = id;

		this.contas = new HashMap<>();
		this.contas.put(TipoDeConta.CORRENTE, new ArrayList<>());
		this.contas.put(TipoDeConta.POUPANCA, new ArrayList<>());
	}

	// getters ////
	public String getNome() { return this.nome; }
	public int getCpf()     { return this.CPF; }
	public int getId()      { return this.ID; }
	public int getNumeroDeContasCorrente() {
		return this.numeroDeContasCorrente;
	}
	public int getNumeroDeContasPoupanca() {
		return this.numeroDeContasPoupanca;
	}
	public ArrayList<Conta> getContasCorrente() {
		return this.contas.get(TipoDeConta.CORRENTE);
	}
	public ArrayList<Conta> getContasPoupanca() {
		return this.contas.get(TipoDeConta.POUPANCA);
	}
	//// getters

	public int menu(Scanner entrada, final ArrayList<Cliente> clientes) {
		int escolha = -1;
		System.out.print("Escolha: ");
		escolha = Integer.parseUnsignedInt(entrada.nextLine());

		switch(escolha) {
		case 1: { // acessar conta
			acessarConta(entrada, clientes);
			break;
		}			

		case 2: { // criar conta
			criarConta(entrada);
			break;
		}

		case 3: { // info do cliente 
			mostrarInfo();
			break;
		}

		case 4: { // mostrar contas
			mostrarContas();
			break;
		}
		
		case 0: { // sair
			System.out.printf("Tchau, %s!%n",
							  this.nome);
			break;
		}

		default: { // invalido
			System.out.println("Opção inválida.");
			break;
		}
		}

		return escolha;
	}	

	private void acessarConta(Scanner entrada, ArrayList<Cliente> outrosClientes) {
		int id = -1;
		Conta conta = null;
		
		if (outrosClientes == null) {
			System.out.println("ERRO: nenhum conta cadastrada.");
			return ;
		}

		System.out.print("Identificador: ");
		id = Integer.parseUnsignedInt(entrada.nextLine());

		conta = acessarConta(id);		
		if ( conta == null ) {
			System.out.printf("ERRO: nenhum conta encontrada com id: '%d'.%n", id);
		} else {
			operarConta(conta, outrosClientes, entrada);
		}
	}

	private void operarConta(Conta conta, ArrayList<Cliente> outrosClientes, Scanner entrada) {
			while (conta.menu(entrada, outrosClientes) != 0);
		}
	
	private Conta acessarConta(int id) {
		if (this.contas.get(TipoDeConta.CORRENTE).isEmpty() &&
			this.contas.get(TipoDeConta.POUPANCA).isEmpty()) {
			return null;	
		}

		for (TipoDeConta t : TipoDeConta.values()) {
			ArrayList<Conta> contas = this.contas.get(t);
			if (contas == null) continue;
			for (Conta conta : contas) {
				if (conta.getId() == id) return conta;
			}
		}	

		return null;
	}

	private void criarConta(Scanner entrada) {
		TipoDeConta t = null;
		int agencia = -1;

		int escolha = -1;	
		System.out.println("Conta");
		System.out.println("    (1) Corrente");
		System.out.println("    (2) Poupança");
		System.out.print("- ");
		escolha = Integer.parseUnsignedInt(entrada.nextLine());	
		if (escolha == 1) { // corrente
			final int id = ++this.numeroDeContasCorrente;
			if (id > this.MAXIMO_DE_CONTAS_CORRENTE) {
				System.out.println("ERRO: limite de contas corrente já foi alcançado"); 
				return ;
			}
			t = TipoDeConta.CORRENTE;
			
			System.out.print("Agência: ");
			agencia = Integer.parseUnsignedInt(entrada.nextLine());
			this.contas.get(t).add( new ContaCorrente(id, agencia) );
			++this.numeroDeContasCorrente;
		} else if (escolha == 2) { // poupanca
			final int id = ++this.numeroDeContasPoupanca;
			if (id > this.MAXIMO_DE_CONTAS_POUPANCA) {
				System.out.println("ERRO: limite de contas poupança já foi alcançado"); 
				return ;
			}
			t = TipoDeConta.POUPANCA;
			
			System.out.print("Agência: ");
			agencia = Integer.parseUnsignedInt(entrada.nextLine());
			this.contas.get(t).add( new ContaPoupanca(id, agencia) );
			++this.numeroDeContasPoupanca;
		} else {
			System.out.printf("ERRO: esperado '1' ou '2', mas temos '%d'.%n", 
							   escolha);
		}
	}
	
	private void mostrarInfo() {
		return ;
	}

	private void mostrarContas() {
		return ;
	}

	@Override
	public String toString() {
		return String.format(
			"Nome: %s%nCPF: %d%nIdentificador: %d%n", this.nome, this.CPF, this.ID
		);
	}
}
