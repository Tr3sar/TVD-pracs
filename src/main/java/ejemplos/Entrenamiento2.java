package ejemplos;

public class Entrenamiento2 {
    public static void main(String[] args)
    {
        int x = 0;
        if (x<=10)
        {
            x++;
        }
        else
        {
            x--;
            if (x<=10)
            {
                x++;
            }
            else
            {
                x--;
            }
        }
    }
}