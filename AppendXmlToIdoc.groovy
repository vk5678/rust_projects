import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

    // Get the IDoc XML from message body
    def body = message.getBody(String.class)

    // Get target segment name from property (default to E1EDK01)
    def targetSegment = message.getProperty("targetSegment") ?: "E1EDK01"

    // Get XML content to append from property
    def xmlToAppend = message.getProperty("xmlToAppend")

    if (!xmlToAppend) {
        throw new Exception("Property 'xmlToAppend' is required but not found")
    }

    // Parse the IDoc XML
    def idocXml = new XmlSlurper().parseText(body)

    // Find the target segment and append the XML content
    def targetNode = idocXml.'**'.find { it.name() == targetSegment }

    if (!targetNode) {
        throw new Exception("Target segment '${targetSegment}' not found in IDoc")
    }

    // Parse the XML content to append
    def appendXml = new XmlSlurper().parseText(xmlToAppend)

    // Append the XML content to target segment
    targetNode.appendNode(appendXml)

    // Convert back to XML string
    def result = groovy.xml.XmlUtil.serialize(idocXml)

    // Set the modified IDoc back to message body
    message.setBody(result)

    return message
}
