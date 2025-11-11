import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

    // Get the PDF split XML from message body using Reader (streaming)
    def pdfSplitReader = message.getBody(java.io.Reader.class)

    // Get IDoc XML from property
    def idocXmlString = message.getProperty("idocXml")

    if (!idocXmlString) {
        throw new Exception("Property 'idocXml' is required but not found")
    }

    // Get target segment name from property (default to E1EDK01)
    def targetSegment = message.getProperty("targetSegment") ?: "E1EDK01"

    // Parse the PDF split XML using Reader (streaming approach)
    def pdfSplitXml = new XmlSlurper().parse(pdfSplitReader)

    // Parse the IDoc XML from property
    def idocXml = new XmlSlurper().parseText(idocXmlString)

    // Find the target segment and append the PDF split content
    def targetNode = idocXml.'**'.find { it.name() == targetSegment }

    if (!targetNode) {
        throw new Exception("Target segment '${targetSegment}' not found in IDoc")
    }

    // Append each ZEINV_PDF segment to target segment
    pdfSplitXml.children().each { child ->
        targetNode.appendNode(child)
    }

    // Convert back to XML string
    def result = groovy.xml.XmlUtil.serialize(idocXml)

    // Set the modified IDoc back to message body
    message.setBody(result)

    return message
}
