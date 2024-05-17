package banco.cliente;

public class ContaPoupanca extends Conta {
	public ContaPoupanca(int id, int agencia) {
		super(id, agencia);
	}

	@Override
	public String toString() {
		return String.format(
			"Tipo: Poupança%nID: %s%nAgência: %d%nSaldo: %.2f%n", 
			this.ID, this.agencia, this.saldo
		);
	}
}
