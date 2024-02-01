package ejemplos;


public class Test_7 {

	public static void main(String[] args)
	{		
	int x=1;						//1
		
		if (x==1)					//2
		{
			x=2;					//3
		}
		else x=3;					//4
		x=4;						//5
		if (x==2)					//6
		{
			x=5;					//7
		}
		else if (x==3) x=6;			//8 i 9
		x=7;						//10
	}
}
