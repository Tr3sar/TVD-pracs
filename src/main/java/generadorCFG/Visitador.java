package generadorCFG;

import cfg.ArcoCFG;
import cfg.NodoCFG;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import cfg.CFG;

import java.util.List;


public class Visitador extends ModifierVisitor<CFG>
{
	/********************************************************/
	/********************** Atributos ***********************/
	/********************************************************/


	/********************************************************/
	/*********************** Metodos ************************/
	/********************************************************/

	// Visitador de métodos
	// Este visitador añade el nodo final al CFG
	@Override
	public Visitable visit(MethodDeclaration methodDeclaration, CFG cfg) {
		// Visitamos el método
		Visitable v = super.visit(methodDeclaration, cfg);

		// Añadimos el nodo final al CFG
		cfg.añadirNodoFinal();

		return v;
	}

	// Visitador de expresiones
	// Cada expresión encontrada genera un nodo en el CFG
	@Override
	public Visitable visit(ExpressionStmt es, CFG cfg) {
		// Creamos el nodo actual
		cfg.crearNodo(es);

		return super.visit(es, cfg);
	}

	@Override
	public Visitable visit(IfStmt is, CFG cfg) {
		cfg.crearNodo(is.getCondition());

		NodoCFG nodoIf = cfg.getNodoActual();

		if (cfg.getNumIfs() == 0) {
			cfg.increaseNumIfs();
		} else {
			cfg.addNodoAnterior(nodoIf);
		}

		is.getThenStmt().accept(this, cfg);

		NodoCFG nodoThen = cfg.getNodoActual();
		cfg.setNodoAnterior(nodoIf);

		NodoCFG nodoElse = null;

		if (!is.hasElseBranch()) {
			cfg.addNodoAnterior(nodoThen);
			cfg.setEsSecuencial(false);
		} else {
			cfg.increaseElsesLeft();
			is.getElseStmt().get().accept(this, cfg);
			nodoElse = cfg.getNodoActual();
			cfg.addNodoAnterior(nodoThen);
			cfg.addNodoAnterior(nodoElse);
			cfg.setEsSecuencial(false);
			cfg.decreaseElsesLeft();
		}

		cfg.decreaseNumIfs();

		return is;
	}


	// Visitador de expresiones WHILE
	@Override
	public Visitable visit(WhileStmt es, CFG cfg) {
		cfg.crearNodo(es.getCondition());

		NodoCFG nodoCondicion = cfg.getNodoActual();

		es.getBody().accept(this, cfg);

		//NodoCFG nodoFinalCuerpo = cfg.getNodoActual();

		if (cfg.getEsSecuencial()) {
			cfg.crearArcoDirigido(cfg.getNodoAnterior(), nodoCondicion);
		} else {
			for(NodoCFG nodoAnterior: cfg.getNodosAnteriores()) {
				cfg.crearArcoDirigido(nodoAnterior, nodoCondicion);
			}

			cfg.setEsSecuencial(true);
			cfg.removeNodosAnteriores();
		}

		cfg.setNodoAnterior(nodoCondicion);

		return es;
	}

	// Visitador de expresiones FOR
	@Override
	public Visitable visit(ForStmt fs, CFG cfg) {

		cfg.crearNodo(fs.getInitialization());
		NodoCFG nodoInicializacion = cfg.getNodoActual();
		cfg.setNodoAnterior(nodoInicializacion);

		cfg.crearNodo(fs.getCompare().get());
		NodoCFG nodoCondicion = cfg.getNodoActual();

		fs.getBody().accept(this, cfg);
		//NodoCFG nodoFinalFor = cfg.getNodoActual();

		cfg.crearNodo(fs.getUpdate());
		NodoCFG nodoActualizacion = cfg.getNodoActual();
		cfg.crearArcoDirigido(nodoActualizacion, nodoCondicion);
		cfg.setNodoAnterior(nodoCondicion);

		return fs;
	}

	//Visitador de expresiones DO
	@Override
	public Visitable visit(DoStmt ds, CFG cfg) {

		Statement cuerpo = ds.getBody();
		Statement primeraInstruccion = cuerpo;
		NodoCFG nodoInicial = null;
		if (cuerpo instanceof BlockStmt) {
			BlockStmt bloque = (BlockStmt) cuerpo;

			for(int i = 0; i < bloque.getStatements().size(); i++) {

				Statement instruccion = bloque.getStatement(i);
				instruccion.accept(this, cfg);

				if (i == 0) {
					nodoInicial = cfg.getNodoActual();
				}
			}
		}

		//ds.getBody().accept(this, cfg);

		cfg.crearNodo(ds.getCondition());
		NodoCFG nodoCondicion = cfg.getNodoActual();

		cfg.crearArcoDirigido(nodoCondicion, nodoInicial);

		cfg.setNodoAnterior(nodoCondicion);

		return ds;
	}

	//Visitador de expresiones SWITCH
	@Override
	public Visitable visit(SwitchStmt ss, CFG cfg) {
		//Aún no funciona correctamente, falta unir todas las salidas de los CASE a la siguiente instrucción

		cfg.crearNodo(ss.getSelector());
		NodoCFG nodoSwitch = cfg.getNodoActual();

		for (SwitchEntry entry : ss.getEntries()) {
			cfg.setNodoAnterior(nodoSwitch);

			String etiquetaCase = entry.getLabels().size() > 0 ?
					"Case " + entry.getLabels().get(0) :
					"Default";
			cfg.crearNodo(etiquetaCase);
			NodoCFG nodoCase = cfg.getNodoActual();

			for (Statement statement : entry.getStatements()) {
				statement.accept(this, cfg);
			}

			NodoCFG ultimoNodoCaso  = cfg.getNodoActual();
			cfg.addNodoAnterior(ultimoNodoCaso);
			cfg.setEsSecuencial(false);
		}

		return ss;
	}

}
