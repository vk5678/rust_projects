# SAP CPI Groovy Script - Append XML to IDoc

## Overview
This Groovy script appends XML content to an existing IDoc at a specified segment location in SAP Cloud Platform Integration (CPI).

## Files
- `AppendXmlToIdocSimple.groovy` - Recommended simple and efficient implementation

## Usage

### Properties Required
1. **xmlToAppend** (required) - The XML content to append to the IDoc
2. **targetSegment** (optional) - The IDoc segment name where content should be appended (default: `E1EDK01`)

### How to Use in SAP CPI

1. Add a **Groovy Script** step in your iFlow
2. Upload the `AppendXmlToIdocSimple.groovy` script
3. Before the Groovy Script step, set the required properties:
   - Set `xmlToAppend` property with the XML content
   - Optionally set `targetSegment` property (defaults to E1EDK01)

### Example

#### Input IDoc (in message body):
```xml
<?xml version="1.0"?>
<ORDERS05>
  <IDOC>
    <EDI_DC40>
      <TABNAM>EDI_DC40</TABNAM>
      <DOCNUM>0000000001</DOCNUM>
    </EDI_DC40>
    <E1EDK01>
      <ACTION>004</ACTION>
      <CURCY>USD</CURCY>
    </E1EDK01>
  </IDOC>
</ORDERS05>
```

#### Property Settings:
- **targetSegment**: `E1EDK01`
- **xmlToAppend**:
```xml
<ROOT>
  <E1EDK14>
    <QUALF>001</QUALF>
    <ORGID>1000</ORGID>
  </E1EDK14>
  <E1EDK14>
    <QUALF>002</QUALF>
    <ORGID>2000</ORGID>
  </E1EDK14>
</ROOT>
```

#### Output:
The script will append the E1EDK14 segments under the E1EDK01 segment.

### Setting Properties in iFlow

You can set properties using:
1. **Content Modifier** - Set property `xmlToAppend` and optionally `targetSegment`
2. **Script** - Use `message.setProperty("xmlToAppend", xmlContent)`

## Features
- Simple and efficient - no complex calculations
- Streaming approach using `java.io.Reader` for better memory efficiency
- Default segment name (E1EDK01) with override capability
- Clear error messages if segment not found
- Handles multiple child nodes in the XML to append
- Uses native Groovy XML parsing (XmlSlurper)

## Error Handling
- Throws exception if `xmlToAppend` property is missing
- Throws exception if target segment not found in IDoc
- Clear error messages for troubleshooting

## Performance
- Uses `java.io.Reader` for streaming input (avoids loading entire IDoc as String)
- Efficient for large IDoc files with minimal memory footprint
- Uses XmlSlurper for efficient XML parsing
- Direct node appending without unnecessary iterations
