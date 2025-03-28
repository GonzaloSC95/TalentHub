package unir.reto.talenthub.entity;

public enum Estado {
    PRESENTADA(0),
    ADJUDICADA(1);

    private final int valor;

    Estado(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

}
