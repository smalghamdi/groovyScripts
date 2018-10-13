// calculates the WMCOnto, DITOnto  and TMOnto of an ontology 
// to run:
// groovy OQuaRE_measures ontology_path

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
hasParent=[:]
hasChild=[:]

for (final OWLSubClassOfAxiom subClasse : ont.getAxioms(AxiomType.SUBCLASS_OF))
{
    if (subClasse.getSuperClass() instanceof OWLClass && subClasse.getSubClass() instanceof OWLClass && subClasse.getSuperClass()!=subClasse.getSubClass())
    {
        if (numberOfParents.containsKey(subClasse.getSubClass())) {
            numberOfParents[subClasse.getSubClass()]++
        }else{
            numberOfParents[subClasse.getSubClass()]=1
        }

        if(hasParent.containsKey(subClasse.getSubClass())){
            hasParent[subClasse.getSubClass()].add(subClasse.getSuperClass())
        }else{
            hasParent[subClasse.getSubClass()]=[subClasse.getSuperClass()]
        }


        if(hasChild.containsKey(subClasse.getSuperClass())){
            hasChild[subClasse.getSuperClass()].add(subClasse.getSubClass())
        }else{
            hasChild[subClasse.getSuperClass()]=[subClasse.getSubClass()]
        }

    }
}


//println (hasChild[fac.getOWLThing()].toString())
numberOfLeafs = 0
//numberOfCompositeLeafs = 0
leafs=[]
//CompositeLeafs=[]
depthOfpath=[:]
//depthOfCompositepath=[:]

ont.getClassesInSignature(true).each {cl ->
    if(!(hasChild[cl] instanceof List)){
        numberOfLeafs++
        leafs.add(cl)
 //       if(cl.toString().indexOf("phenomebrowser")>-1){
 //           numberOfCompositeLeafs++
 //           CompositeLeafs.add(cl)
 //       }
    }

}

println("number of leafs = " + numberOfLeafs)
sumDepth = 0
leafs.each{ cl ->
    node = cl
    depth = 0
    while(node!=null){
        node = (hasParent[node] instanceof List)? hasParent[node][0]:null
        depth++
        //println(node.toString() + "    = " +depth-1)

    }
    //println(cl.toString() + "    = " +(depth-1))
    depthOfpath[cl]=(depth-1)
    sumDepth+=(depth-1)
}

/*
sumCompositeDepth = 0
CompositeLeafs.each{ cl ->
    node = cl
    depth = 0
    while(node!=null){
        node = (hasParent[node] instanceof List)? hasParent[node][0]:null
        depth++
        //println(node.toString() + "    = " +depth-1)

    }
    //println(cl.toString() + "    = " +(depth-1))
    depthOfCompositepath[cl]=(depth-1)
    sumCompositeDepth+=(depth-1)
}
*/

println("WMCOnto mean depth of paths = " + sumDepth/numberOfLeafs)
println("DITOnto maximum depth = " + depthOfpath.max{it.value})
/*
println("WMCOnto (Composite) mean depth of paths = " + sumCompositeDepth/numberOfCompositeLeafs)
println("DITOnto (Composite) maximum depth = " + depthOfCompositepath.max{it.value})
*/
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
println("TMOnto Tangledness = " + (ClassesWithMoreThanOneDirectedParent/(numberOfClasses-1)))


println("done")
