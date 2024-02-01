package ejemplos;

public class Test_SWITCH {
    public static void main(String[] args) {
        String palabra = "Hola";

        switch(palabra.length()) {
            case 1:
                System.out.println("La longitud es 1");
                break;
            case 2:
                System.out.println("La longitud es 2");
                break;
            case 3:
                System.out.println("La longitud es 3");
                break;
            case 4:
                System.out.println("La longitud es 4");
                break;
            default:
                palabra = "trombo";
                break;
        }

        System.out.println("Switch terminado");


    }
}
