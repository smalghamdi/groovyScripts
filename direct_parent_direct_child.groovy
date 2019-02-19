
//-----------------------------------------------------------
// Sarah M. Algamdi
//-----------------------------------------------------------
// This code generates two dictionaries in JSON formats of classes in an ontology, 
// 'direct_children.json' maps a class to list of children
// 'direct_parents.json' maps the class to list of parents 
// to run:
// groovy direct_parent_direct_child.groovy ontology_path
//----------------------------------------------------------- 


@Grapes([
 @Grab(group='org.slf4j', module='slf4j-simple', version='1.6.1'),
 @Grab(group = 'org.semanticweb.elk', module = 'elk-owlapi', version = '0.4.2'),
 @Grab(group = 'net.sourceforge.owlapi', module = 'owlapi-api', version = '4.2.5'),
 @Grab(group = 'net.sourceforge.owlapi', module = 'owlapi-apibinding', version = '4.2.5'),
 @Grab(group = 'net.sourceforge.owlapi', module = 'owlapi-impl', version = '4.2.5'),
 @Grab(group = 'net.sourceforge.owlapi', module = 'owlapi-parsers', version = '4.2.5'),
 @GrabConfig(systemClassLoader = true)
])


import org.semanticweb.owlapi.model.parameters.*
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.elk.owlapi.ElkReasonerConfiguration
import org.semanticweb.elk.reasoner.config.*
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.reasoner.*
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.io.*;
import org.semanticweb.owlapi.owllink.*;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.search.*;
import org.semanticweb.owlapi.manchestersyntax.renderer.*;
import org.semanticweb.owlapi.reasoner.structural.*
import groovy.json.JsonOutput
import java.io.File 


OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
OWLDataFactory fac = manager.getOWLDataFactory()
StructuralReasonerFactory f1 = new StructuralReasonerFactory()



ont =manager.loadOntologyFromOntologyDocument(new File(args[0]))


ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor()
OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor)
ElkReasonerFactory f = new ElkReasonerFactory()
OWLReasoner reasoner = f.createReasoner(ont,config)

direct_parents =[:]
direct_children = [:]


def classToId(cl){
	cl.toString().replaceAll('<','').replaceAll('>','')

}


ont.getClassesInSignature(true).each {cl ->

	clID = classToId(cl)
	print(clID)
	direct_children[clID] = []
	direct_parents[clID] = []

	reasoner.getSubClasses(cl, true).each { n-> n.each{ child->
	    direct_children[clID].add(classToId(child))
	} }


	reasoner.getSuperClasses(cl, true).each { n-> n.each{ parent->
	    direct_parents[clID].add(classToId(parent))
	} }
}




def output = JsonOutput.toJson(direct_children)
new File('direct_children.json').withWriter('utf-8') { 
         writer -> writer.writeLine output
}

output = JsonOutput.toJson(direct_parents)
new File('direct_parents.json').withWriter('utf-8') { 
         writer -> writer.writeLine output
}
