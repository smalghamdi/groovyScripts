@Grapes([
          @Grab(group='org.semanticweb.elk', module='elk-owlapi', version='0.4.2'),
          @Grab(group='net.sourceforge.owlapi', module='owlapi-api', version='4.2.5'),
          @Grab(group='net.sourceforge.owlapi', module='owlapi-apibinding', version='4.2.5'),
          @Grab(group='net.sourceforge.owlapi', module='owlapi-impl', version='4.2.5'),
          @Grab(group='net.sourceforge.owlapi', module='owlapi-parsers', version='4.2.5'),
          @GrabConfig(systemClassLoader=true)
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


OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
OWLOntology outont = manager.createOntology(IRI.create("http://phenotypes_from_HPO.owl"))
def onturi = "http://phenotypes_from_HPO.owl#"


OWLDataFactory fac = manager.getOWLDataFactory()
ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor()
OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor)
ElkReasonerFactory f = new ElkReasonerFactory()

input_Ontology = manager.loadOntologyFromOntologyDocument(IRI.create("http://purl.obolibrary.org/obo/hp.owl"))

OWLReasoner reasoner = f.createReasoner(input_Ontology,config)
def o_class = fac.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/HP_0000118"))

input_Ontology.getAxioms(o_class).each{ax ->
    manager.addAxiom(outont, ax)
}
EntitySearcher.getAnnotationAssertionAxioms(o_class, input_Ontology).each { ax ->
	manager.addAxiom(outont, ax)
}


classes = reasoner.getSubClasses(o_class, false).getFlattened()


classes.each{ cl ->
        input_Ontology.getAxioms(cl).each{ax ->
            manager.addAxiom(outont, ax)
        }
        EntitySearcher.getAnnotationAssertionAxioms(cl, input_Ontology).each { ax ->
   			manager.addAxiom(outont, ax)
  		}
    
}

manager.saveOntology(outont, IRI.create((new File("phenotypes_from_HPO.owl").toURI())))
