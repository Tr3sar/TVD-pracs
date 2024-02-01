package ejemplos;

public class Test_Clase {
    public static void main(String[] args)
    {
        int y = 0;
        int x = 0;

        if (x > 0) {
            if (y > 0) {
                x++;
            } else {
                y++;
            }
        } else {
            if (x > 3) {
                x++;
            }
        }
        x++;
    }
}
