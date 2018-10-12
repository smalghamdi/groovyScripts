// calculates the tangleness of an ontology 
// to run:
// groovy tangleness.groovy ontology_path

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



//load ontology
def ont = manager.loadOntologyFromOntologyDocument(new File(args[0]))

numberOfParents = [:]

for (final OWLSubClassOfAxiom subClasse : ont.getAxioms(AxiomType.SUBCLASS_OF))
{
    if (subClasse.getSuperClass() instanceof OWLClass && subClasse.getSubClass() instanceof OWLClass)
    {
        if (numberOfParents.containsKey(subClasse.getSubClass())) {
            numberOfParents[subClasse.getSubClass()]++
        }else{
            numberOfParents[subClasse.getSubClass()]=1
        }

        //println(subClasse.getSubClass().toString() + " extends " + subClasse.getSuperClass().toString());
    }
}

numberOfClasses =0
ont.getClassesInSignature(true).each {
    numberOfClasses++
}

ClassesWithMoreThanOneDirectedParent=0
numberOfParents.keySet().each{ cl ->
    if(numberOfParents[cl] > 1){ClassesWithMoreThanOneDirectedParent++}
    //println(cl.toString()+"\t"+numberOfParents[cl])
}

println("numberOfClasses = " +numberOfClasses)
println("ClassesWithMoreThanOneDirectedParent = " +ClassesWithMoreThanOneDirectedParent)
println("Tangledness = " + (ClassesWithMoreThanOneDirectedParent/(numberOfClasses-1)))


println("done")
