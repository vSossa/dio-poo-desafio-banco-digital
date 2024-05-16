package banco;

import banco.cliente.Cliente;

import java.util.ArrayList;
import java.util.Scanner;

public class Banco {
	private int ID;
	private String nome;
	private ArrayList<Cliente> clientes;

	private int numeroDeClientesCadastrados = 0;
	private final int NUMERO_MAXIMO_DE_CLIENTES = 10;

	public Banco(String nome, int id) {
		this.nome = nome;
		this.ID = id;

		this.clientes = new ArrayList<>();
	}

	public int getNumeroDeClientesCadastrados() { return this.numeroDeClientesCadastrados; }

	public void menu() {
		Scanner entrada = new Scanner(System.in);

		int escolha = -1;
		System.out.println("========= Bem vindo =========");
		System.out.println("========= " + this.nome);
		do {
			System.out.println();
			System.out.println("(1) Acessar conta");
			System.out.println("(2) Criar conta");
			System.out.println("(3) Mostrar contas");
			System.out.println("(0) Sair");
			System.out.print("- Escolha: ");
			try { 
				escolha = Integer.parseUnsignedInt(entrada.nextLine());
			} catch(NumberFormatException e) {
				System.out.println("ERRO: formato inválido para escolha.");
			}
			switch(escolha) {
			case 1: { // acessar
				acessarCliente(entrada);
				break;
			} 
		
			case 2: { // abrir
				cadastrarCliente(entrada);
				break;
			}

			case 3: { // info 
				mostrarClientes();
				break;
			}

			case 0: { // sair
				System.out.println();
				System.out.println("========= Até mais");
				break;
			}

			default: { // invalido
				System.out.println();
				System.out.println("Opção inválida.");
				break;
			}
			}
		} while (escolha != 0);
		entrada.close();
	}

	public void acessarCliente(Scanner entrada) {
		int cpf = -1;
		Cliente cliente = null;
		
		System.out.println();
		System.out.println("========= Acesso");
		System.out.print("CPF: ");	
		try {
			cpf = Integer.parseUnsignedInt(entrada.nextLine());
		} catch(NumberFormatException e) {
			System.out.println();
			System.out.println("ERRO: formato inválido para CPF.");
			System.out.println();
		}

		cliente = acessarCliente(cpf);
		if ( cliente == null ) {
			System.out.println();
			System.out.printf("ERRO: '%d' não está cadastrado.%n",
						      cpf);	 
			System.out.println();
		} else {
			operarContaCliente(cliente, entrada);	
		}
		System.out.println("=========");
	}	

	private void operarContaCliente(Cliente cliente, Scanner entrada) {
		System.out.println();
		System.out.println("========= Menu");
		while (cliente.menu(entrada, this.clientes) != 0);
	}
	
	private Cliente acessarCliente(int cpf) {
		if (this.clientes.isEmpty()) return null;
		
		for (Cliente cliente : this.clientes) {
			if (cliente.getCpf() == cpf) return cliente;
		}

		return null;
	}

	public void cadastrarCliente(Scanner entrada) {
		String nome;
		int cpf = -1;
		int id = ++this.numeroDeClientesCadastrados;

		System.out.println();
		System.out.println("========= Cadastro");
		if (id > this.NUMERO_MAXIMO_DE_CLIENTES) { 
			System.out.println();
			System.out.println("ERRO: não é possível fazer um cadastro: limite de clientes atingido.");
			System.out.println();
			return ;
		}

		// TODO: impedir nomes estranhos: caracteres estranhos, muito grandes ou muito pequenos,
		// e numeros.
		System.out.print("Nome: ");
		nome = entrada.nextLine();

		System.out.print("CPF: ");
		try {
			cpf = Integer.parseUnsignedInt(entrada.nextLine());
		} catch(NumberFormatException e) {
			System.out.println();
			System.out.println("ERRO: formato inválido para CPF.");
			System.out.println();
		}

		if ( !cadastrarCliente(nome, cpf, id) ) {
			System.out.println();
			System.out.printf("ERRO: '%d' já está cadastrado.%n", cpf);
			System.out.println();
		} else {
			System.out.println("Cadastro completo com sucesso.");
			this.numeroDeClientesCadastrados++;
		}
	} 

	private boolean cadastrarCliente(String nome, int cpf, int id) {
		for (Cliente cliente : this.clientes) {
			if (cliente.getCpf() == cpf) return false;
		}

		this.clientes.add( new Cliente(nome, cpf, id) );
		this.numeroDeClientesCadastrados++;
		return true;
	}

	// NOTA: no mundo real, não poderíamos ver quem são os outros
	// clientes do banco, mas, para simplificar, permiteremos isto
	// aqui
	private void mostrarClientes() {
		System.out.println();
		System.out.println("========= Clientes");
		if (this.clientes.isEmpty()) {
			System.out.println("Nenhum cliente cadastrado.");
			return ;
		}		

		for (Cliente cliente : this.clientes) {
			System.out.println("" + cliente);
		}
	}
} 
