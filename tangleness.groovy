// calculates the tangleness of an ontology 
// to run:
// groovy tangleness.groovy ontology_path

@Grapes([ 
@Grab(group='org.slf4j', module='slf4j-simple', version='1.6.1'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-api', version='4.2.5'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-apibinding', version='4.2.5')
])

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import java.io.File;
import org.semanticweb.owlapi.util.OWLOntologyMerger;

OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
OWLDataFactory fac = manager.getOWLDataFactory()

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
