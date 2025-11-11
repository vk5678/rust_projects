import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil

def Message processData(Message message) {

    // Get the PDF split XML from message body using Reader (streaming)
    def pdfSplitReader = message.getBody(java.io.Reader.class)

    // Get IDoc XML from property
    def idocXmlString = message.getProperty("idocXml")

    if (!idocXmlString) {
        throw new Exception("Property 'idocXml' is required")
    }

    // Get target segment name from property (default to E1EDK01)
    def targetSegment = message.getProperty("targetSegment") ?: "E1EDK01"

    // Parse PDF split XML using Reader (streaming approach)
    def pdfSplitXml = new XmlSlurper().parse(pdfSplitReader)

    // Parse IDoc XML from property
    def idocXml = new XmlSlurper().parseText(idocXmlString)
    idocXml.setProperty("keepIgnorableWhitespace", false)

    // Find the target segment in IDoc
    def targetNode = idocXml.depthFirst().find { node ->
        node.name() == targetSegment
    }

    if (!targetNode) {
        throw new Exception("Segment '${targetSegment}' not found in IDoc")
    }

    // Append each ZEINV_PDF segment to the target segment
    pdfSplitXml.children().each { child ->
        targetNode.appendNode(child)
    }

    // Serialize back to XML
    def resultXml = XmlUtil.serialize(idocXml)

    // Set result to message body
    message.setBody(resultXml)

    return message
}
