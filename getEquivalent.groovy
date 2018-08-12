@Grapes([
@Grab(group='org.slf4j', module='slf4j-simple', version='1.6.1'),
@Grab(group='org.semanticweb.elk', module='elk-owlapi', version='0.4.3'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-api', version='4.2.5'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-apibinding', version='4.2.5'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-impl', version='4.2.5'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-parsers', version='4.2.5')
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
import java.io.File;
import org.semanticweb.owlapi.util.OWLOntologyMerger;



OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
OWLDataFactory fac = manager.getOWLDataFactory()


ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor()
OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor)
ElkReasonerFactory f = new ElkReasonerFactory()


//load ontology
def ont = manager.loadOntologyFromOntologyDocument(new File(args[0]))
OWLReasoner reasoner = f.createReasoner(ont,config)



def id = {cl->cl
    return cl.substring(cl.lastIndexOf("/")+1,cl.length()-1).replace("_",":")
}

ont.getClassesInSignature(true).each { cl ->
    if(cl.toString().indexOf(args[1]+"_")>-1){
        EntitySearcher.getEquivalentClasses(cl, ont).each { cExpr ->
            if (cExpr.isClassExpressionLiteral()) {
                if(cExpr.toString().indexOf(args[2]+"_")>-1){
                    println(id(cl.toString())+" equivalent to "+id(cExpr.toString()))
                }
            }
        }

    }
}

println("done")