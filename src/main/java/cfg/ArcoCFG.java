package cfg;

public class ArcoCFG {
	NodoCFG nodoInicial;
	NodoCFG nodoFinal;
	
	ArcoCFG (NodoCFG nodoInicial, NodoCFG nodoFinal){
		this.nodoInicial = nodoInicial;
		this.nodoFinal = nodoFinal;
	}
	
	String imprimir(){
		return "\"" + nodoInicial.imprimir() + "\"" +  " -> " + "\"" + nodoFinal.imprimir() + "\""; 
	}
}
