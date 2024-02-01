package cfg;

public class NodoCFG {
	int id;
	String texto = "NEW NODE";
	
	NodoCFG(int id, String texto){
		this.id=id;
		this.texto=texto;
	}
	
	String imprimir() {
		return "("+id+") "+texto; 
	}
}

