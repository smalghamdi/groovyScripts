//-----------------------------------------------------------
// Sarah M. Algamdi
//-----------------------------------------------------------
// This code is used to find exact synonyms of classes in an ontology, 
// outputs three columns tab seperated (class id, class label, synonyms) 
// to run:
// findExactSynonyms.groovy ontology_path
//-----------------------------------------------------------


@Grapes([
@Grab(group='org.slf4j', module='slf4j-simple', version='1.6.1'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-api', version='4.2.5'),
@Grab(group='net.sourceforge.owlapi', module='owlapi-apibinding', version='4.2.5')
])

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.search.*;
import java.io.File;


OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
OWLDataFactory fac = manager.getOWLDataFactory()



//load ontology
def ont = manager.loadOntologyFromOntologyDocument(new File(args[0]))

def id = {cl->cl
    return cl.substring(cl.lastIndexOf("/")+1,cl.length()-1).replace("_",":")
}

ont.getClassesInSignature(true).each { cl ->
    //getting the class label
    label = ""
    EntitySearcher.getAnnotationObjects(cl, ont, fac.getRDFSLabel()).each {
        lab ->
        if (lab.getValue() instanceof OWLLiteral) {
            label = lab.toString().split("\"")[1]
        }
    }


    EntitySearcher.getAnnotations(cl, ont).each { ax ->
        if(ax.toString().indexOf("hasExactSynonym")>-1 ){
            println(id(cl.toString())+"\t"+label+"\t"+ax.toString().split(">")[1].split("<")[0].split("\"")[1])

        }
    }

}
println("done")
