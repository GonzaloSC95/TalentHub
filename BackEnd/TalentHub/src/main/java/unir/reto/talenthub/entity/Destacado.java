package unir.reto.talenthub.entity;

public enum Destacado {
   
   NO(0),
   SI(1);
   
   private final int value;
   
   Destacado(int value) {
      this.value = value;
   }
   
   public int getValue() {
      return value;
   }
}
