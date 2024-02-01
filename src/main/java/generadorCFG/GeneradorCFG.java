//////////////////////////////////////////
//                                      //
// Universitat Politècnica de València  //
//                                      //
// GENERADOR DE CFGs DE PROGRAMAS JAVA  //
// Author: Josep Silva Galiana          //
//                                      // 
//////////////////////////////////////////

package generadorCFG;

import cfg.CFG;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Some code that uses JavaParser.
 */
public class GeneradorCFG {
    public static void main(String[] args) {
        
   	// Ruta del fichero con el programa del que vamos a generar el CFG
		String ruta = "src/main/java/ejemplos/";
		String fichero = "Test_Clase";
    			
     	// JavaParser has a minimal logging class that normally logs nothing.
        // Let's ask it to write to standard out:
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        
        Log.info("Generador de CFGs 1.0\n---------------------");
    	
        // SourceRoot is a tool that read and writes Java files from packages on a certain root directory.
        // In this case the root directory is found by taking the root from the current Maven module,
        // with src/main/resources appended.
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(GeneradorCFG.class).resolve(ruta));

    // Parseamos el fichero Java y generamos su AST (su compilation unit).
        CompilationUnit cu = sourceRoot.parse("", fichero+".java");

    // Eliminamos todos los comentarios del fichero fuente (no aparecen en el CFG)        
        quitarComentarios(cu.findRootNode());
        
    // Creamos un objeto CFG, donde almacenaremos el CFG
		CFG cfg = new CFG();

	// Recorremos el AST con el objeto CFG		
		Visitador visitador = new Visitador();
        cu.accept(visitador, cfg);

    // Imprimimos el CFG en un String (en lenguaje DOT) 	
		String dotInfo = cfg.obtenerGrafo();
	// OPCIONAL: Imprimimos el CFG del programa por pantalla
		cfg.imprimirGrafo();
		
		
	// Generamos un PDF con el CFG del programa
		System.out.print("\nGenerando PDF...");
	    GraphViz gv=new GraphViz();
	    gv.addln(gv.start_graph());
	    gv.add(dotInfo);
	    gv.addln(gv.end_graph());
	    String type = "pdf";   // String type = "gif";
	  // gv.increaseDpi();
	    gv.decreaseDpi();
	    gv.decreaseDpi();
	    gv.decreaseDpi();
	    gv.decreaseDpi();
	    File destino_CFG = new File(ruta + fichero + "_CFG."+ type);
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), destino_CFG);
	    System.out.print("   "+ruta + fichero + "_CFG."+ type);
	    System.out.println("     ¡PDF generado!");

		try {
			Desktop.getDesktop().open(new File(ruta + fichero + "_CFG.pdf"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    
	// OPCIONAL: Si hemos modificado el fichero Java con el visitador, podemos 
	// generar un nuevo fichero Java modificado

        // This saves all the files we just read to an output directory.  
//        sourceRoot.saveAll(
//                // The path of the Maven module/project which contains the LogicPositivizer class.
//                CodeGenerationUtils.mavenModuleRoot(LogicPositivizer.class)
//                        // appended with a path to "output"
//                        .resolve(Paths.get("output")));
    }
    
	// Elimina todos los comentarios de un nodo y sus hijos
		static void quitarComentarios(Node node){
			node.removeComment();
			node.removeOrphanComment(null);
			for (Comment comment : node.getOrphanComments())
			{
				node.removeOrphanComment(comment);
			}
		    // Do something with the node
		    for (Node child : node.getChildNodes()){
		    	quitarComentarios(child);
		    }
	    }
}

////////////////////////////////////////////////////////////////
//COMO CONFIGURAR GRAPHVIZ:
////////////////////////////////////////////////////////////////
//
//Update config.properties file. Copy paste the following:
//
//##############################################################
//#                    Linux Configurations                    #
//##############################################################
//# The dir. where temporary files will be created.
//tempDirForLinux = /tmp
//# Where is your dot program located? It will be called externally.
//dotForLinux = /usr/bin/dot
//
//##############################################################
//#                   Windows Configurations                   #
//##############################################################
//# The dir. where temporary files will be created.
//tempDirForWindows = c:/temp
//# Where is your dot program located? It will be called externally.
//dotForWindows = "c:/Program Files (x86)/Graphviz 2.28/bin/dot.exe"
//
//##############################################################
//#                    Mac Configurations                      #
//##############################################################
//# The dir. where temporary files will be created.
//tempDirForMacOSX = /tmp
//# Where is your dot program located? It will be called externally.
//dotForMacOSX = /usr/local/bin/dot
