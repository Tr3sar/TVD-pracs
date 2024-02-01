package cfg;

import java.util.ArrayList;
import java.util.List;

public class CFG {
	
	private List<ArcoCFG> arcos = new ArrayList<ArcoCFG>();

	private List<NodoCFG> nodosAnteriores = new ArrayList<NodoCFG>();
	private int numElsesLeft = 0;
	
	private int idActual = 0;
	private NodoCFG nodoAnterior = null;
	private NodoCFG nodoActual = null;

	private boolean esSecuencial = true;

	
	// Al crear un CFG se crea un nodo "start" y un arco desde ese nodo que apunta a un nodo null
	public CFG(){
		nodoAnterior = new NodoCFG(idActual,"Start");
	}
	
	// Crear nodo
	// Añade un arco desde el nodo actual hasta el último control
	public void crearNodo(Object objeto)
	{
		idActual++;
		nodoActual = new NodoCFG(idActual,quitarComillas(objeto.toString()));
		// OPCIONAL: Imprimir los nodos cada vez que se crean
		//System.out.println("NODO: " + nodoActual.imprimir());
		crearArcos();
		nodoAnterior = nodoActual;
	}
		
	// Sustituye " por \" en un string: Sirve para eliminar comillas.
	private String quitarComillas(String texto)
	{
	    return texto.replace("\"", "\\\"");
	}
	
	// Crear arcos
	private void crearArcos()
	{
			añadirArcoSecuencialCFG();
	}

	
	// Añade un arco desde el último nodo hasta el nodo actual (se le pasa como parametro)
	private void añadirArcoSecuencialCFG()
	{	
		ArcoCFG arco = new ArcoCFG(nodoAnterior,nodoActual);
		arcos.add(arco);

		if (!this.esSecuencial && this.numElsesLeft == 0) {
			for(NodoCFG nodoAnteriorLista: nodosAnteriores) {
				ArcoCFG arcoLista = new ArcoCFG(nodoAnteriorLista, nodoActual);
				arcos.add(arcoLista);
			}
			this.esSecuencial = true;
			this.nodosAnteriores.clear();
		}
	}	
	
	public void añadirNodoFinal() {
		idActual++;
		NodoCFG nodofinal = new NodoCFG(idActual,"Stop");
		ArcoCFG arcofinal = new ArcoCFG(nodoAnterior,nodofinal);

		arcos.add(arcofinal);
	}

	
	// Obtiene el grafo en formato DOT (String)
	public String obtenerGrafo()
	{
		String dotInfo="";
		for(ArcoCFG arco:arcos) {
			dotInfo += arco.imprimir();	
		}
		return dotInfo;
	}
	
	// Imprime el grafo en la pantalla
	public void imprimirGrafo()
	{
		System.out.println("\nARCOS del CFG:");

		String dotInfo="";
		for(ArcoCFG arco:arcos) {
			dotInfo += arco.imprimir();	
			System.out.println("ARCO: "+arco.imprimir());
		}
		System.out.println("\nCFG completo:");
		System.out.println(dotInfo);
	}

	public NodoCFG getNodoActual() {
		return this.nodoActual;
	}

	public void setNodoAnterior(NodoCFG nodo) {
		this.nodoAnterior = nodo;
	}

	public void addNodoAnterior(NodoCFG nodo) {
		if (!this.nodosAnteriores.contains(nodo) && !this.nodoAnterior.equals(nodo)) {
			this.nodosAnteriores.add(nodo);
		}
	}

	public void setEsSecuencial(boolean esSecuencial) {
		this.esSecuencial = esSecuencial;
	}

	public void crearArcoDirigido(NodoCFG nodoInicial, NodoCFG nodoFinal) {
		ArcoCFG arco = new ArcoCFG(nodoInicial, nodoFinal);
		arcos.add(arco);
	}

	public void increaseElsesLeft() {
		this.numElsesLeft++;
	}

	public void decreaseElsesLeft() {
		this.numElsesLeft--;
	}


}
