// use this code to parse the output of AgreementMaker light(AML) in tab seperated table
// to run:
// groovy parseAMLOutput.groovy file_path

file = new File(args[0])
def text=file.text

def al = new XmlSlurper().parseText(text)

Alighnments = al.Alignment
println("First_Ontology_Class\tSecond_Ontology_Class\tScore\tRelation")
Alighnments.map.each{
        m-> 
        println("${m.Cell.entity1.@'rdf:resource'}\t${m.Cell.entity2.@'rdf:resource'}\t${m.Cell.measure}\t${m.Cell.relation}")
}
