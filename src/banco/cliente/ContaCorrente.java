package banco.cliente;

public class ContaCorrente extends Conta {
	public ContaCorrente(int id, int agencia) {
		super(id, agencia);
	}

	@Override
	public String toString() {
		return String.format(
			"Tipo: Corrente%nID: %s%nAgência: %d%nSaldo: %.2f%n", 
			this.ID, this.agencia, this.saldo
		);
	}
}
