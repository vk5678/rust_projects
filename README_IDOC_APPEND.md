# SAP CPI Groovy Script - Append PDF Split to IDoc

## Overview
This Groovy script appends PDF split XML content (ZEINV_PDF segments) to an existing IDoc at a specified segment location in SAP Cloud Platform Integration (CPI).

## Files
- `AppendXmlToIdocSimple.groovy` - Recommended simple and efficient implementation

## Usage

### Input Requirements
1. **Message Body** (required) - The PDF split XML content with ZEINV_PDF segments
2. **idocXml property** (required) - The original IDoc XML
3. **targetSegment property** (optional) - The IDoc segment name where content should be appended (default: `E1EDK01`)

### How to Use in SAP CPI

1. Add a **Content Modifier** step before the Groovy Script:
   - Save the IDoc XML to `idocXml` property
   - Optionally set `targetSegment` property (defaults to E1EDK01)
2. Set the message body to the PDF split XML (ZEINV_PDF segments)
3. Add a **Groovy Script** step and upload `AppendXmlToIdocSimple.groovy`

### Example

#### Input - Message Body (PDF Split XML):
Note: The PDF split XML doesn't need a root element. The script automatically handles this.

```xml
<ZEINV_PDF SEGMENT="1">
  <FILE>Constant</FILE>
</ZEINV_PDF>
<ZEINV_PDF SEGMENT="2">
  <FILE>Constant</FILE>
</ZEINV_PDF>
```

#### Input - Property Settings:
- **idocXml** (required):
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
- **targetSegment** (optional): `E1EDK01` (default)

#### Output (in message body):
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
      <ZEINV_PDF SEGMENT="1">
        <FILE>Constant</FILE>
      </ZEINV_PDF>
      <ZEINV_PDF SEGMENT="2">
        <FILE>Constant</FILE>
      </ZEINV_PDF>
    </E1EDK01>
  </IDOC>
</ORDERS05>
```

The script appends the ZEINV_PDF segments from the body to the E1EDK01 segment in the IDoc.

### Setting Properties in iFlow

Use a **Content Modifier** before the Groovy Script step:
1. Set property `idocXml` with the IDoc XML (use `${in.body}` if IDoc is in body initially)
2. Optionally set property `targetSegment` (defaults to E1EDK01 if not specified)
3. Set the message body to the PDF split XML using Body field

**Example Content Modifier Configuration:**
- Action: Create
- Property: `idocXml` = `${in.body}` (save the IDoc)
- Property: `targetSegment` = `E1EDK01` (optional)
- Body: Set to the PDF split XML from another source

## Features
- Simple and efficient - no complex calculations
- Streaming approach using `java.io.Reader` for PDF split XML
- Automatically handles PDF split XML without root element
- Handles multiple ZEINV_PDF segments with SEGMENT attribute
- Default segment name (E1EDK01) with override capability
- Clear error messages if segment not found
- Uses native Groovy XML parsing (XmlSlurper)

## Error Handling
- Throws exception if `idocXml` property is missing
- Throws exception if target segment not found in IDoc
- Clear error messages for troubleshooting

## Performance
- Uses `java.io.Reader` for streaming PDF split input (minimal memory for large PDF splits)
- Efficient XML parsing with XmlSlurper
- Direct node appending without unnecessary iterations
- Optimized for processing multiple ZEINV_PDF segments

## Use Case
This script is designed for scenarios where:
- You have an IDoc that needs PDF split segments appended
- The PDF split contains multiple ZEINV_PDF segments with SEGMENT attributes
- The PDF segments need to be inserted at a specific location in the IDoc hierarchy
- You want to maintain the IDoc structure while adding PDF attachment references
