@Grapes([
@Grab(group='org.slf4j', module='slf4j-simple', version='1.6.1'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-api', version='4.2.5'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-apibinding', version='4.2.5'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-impl', version='4.2.5'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-parsers', version='4.2.5'),
@Grab(group='net.sourceforge.owlapi', module='org.semanticweb.hermit', version='1.3.8.413')
])

import org.semanticweb.owlapi.model.parameters.*
import org.semanticweb.HermiT.ReasonerFactory;
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
ReasonerFactory rf = new ReasonerFactory()

Set<OWLOntology> onts = [manager.loadOntologyFromOntologyDocument(new File(args[0]))]

for (int i = 1; i < args.length; i++) {
     println(i+" "+args[i])
     onts << manager.loadOntologyFromOntologyDocument(new File(args[i]))
}

def mperged = IRI.create("http://phenomebrowser.net/")

OWLOntology mergedOnt = manager.createOntology(mperged,onts)

OWLReasoner reasoner = rf.createReasoner(mergedOnt,config)

// Add inferred classes using HermiT reasoner
List<InferredAxiomGenerator<? extends OWLAxiom>> generator = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
generator.add(new InferredSubClassAxiomGenerator());
generator.add(new InferredEquivalentClassAxiomGenerator());
InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, generator);
iog.fillOntology(fac, mergedOnt);


manager.saveOntology(mergedOnt,IRI.create((new File("outont.owl").toURI())))
