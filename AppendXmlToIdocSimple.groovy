import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil

def Message processData(Message message) {

    // Get the IDoc XML from message body using Reader (streaming)
    def reader = message.getBody(java.io.Reader.class)

    // Get target segment name from property (default to E1EDK01)
    def targetSegment = message.getProperty("targetSegment") ?: "E1EDK01"

    // Get XML content to append from property
    def xmlToAppend = message.getProperty("xmlToAppend")

    if (!xmlToAppend) {
        throw new Exception("Property 'xmlToAppend' is required")
    }

    // Parse IDoc XML using Reader (streaming approach)
    def idocXml = new XmlSlurper().parse(reader)
    idocXml.setProperty("keepIgnorableWhitespace", false)

    // Parse XML content to append
    def contentToAppend = new XmlSlurper().parseText(xmlToAppend)

    // Find the target segment
    def targetNode = idocXml.depthFirst().find { node ->
        node.name() == targetSegment
    }

    if (!targetNode) {
        throw new Exception("Segment '${targetSegment}' not found in IDoc")
    }

    // Append each child node from the content to the target segment
    contentToAppend.children().each { child ->
        targetNode.appendNode(child)
    }

    // Serialize back to XML
    def resultXml = XmlUtil.serialize(idocXml)

    // Set result to message body
    message.setBody(resultXml)

    return message
}
