
//-----------------------------------------------------------
// Sarah M. Algamdi
//-----------------------------------------------------------
// This code is used to find all subclasses of class of a given ontology 
// using ELK reasoner
// 
// to run:
// groovy getSubclasses.groovy ontology_path class_path
//-----------------------------------------------------------



@Grapes([
	  @Grab(group='org.slf4j', module='slf4j-simple', version='1.6.1'),
          @Grab(group='org.semanticweb.elk', module='elk-owlapi', version='0.4.3'),
          @Grab(group='net.sourceforge.owlapi', module='owlapi-api', version='4.2.5'),
          @Grab(group='net.sourceforge.owlapi', module='owlapi-apibinding', version='4.2.5'),
          @Grab(group='net.sourceforge.owlapi', module='owlapi-impl', version='4.2.5')
        ])

import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.elk.owlapi.ElkReasonerConfiguration
import org.semanticweb.elk.reasoner.config.*
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.reasoner.*
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.io.*;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.reasoner.structural.*
import java.io.File;




OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
OWLDataFactory fac = manager.getOWLDataFactory()


ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor()
OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor)
ElkReasonerFactory f = new ElkReasonerFactory()


//load ontology
def ont = manager.loadOntologyFromOntologyDocument(new File(args[0]))
OWLReasoner reasoner = f.createReasoner(ont,config)

//define existing class
def o_class = fac.getOWLClass(IRI.create(args[1]))

//get all the subclasses of that class using ELK (if you change to true you will get only the direct subclasses)
reasoner.getSubClasses(o_class, false).each { n-> n.each{println it} }
println("done")


