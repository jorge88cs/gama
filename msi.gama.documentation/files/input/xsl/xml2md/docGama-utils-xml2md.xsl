<?xml version="1.0" encoding="UTF-8"?><!---->
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:wiki="www.google.fr">


<xsl:template name="generateExamples">
	<xsl:for-each select="examples/example">
		<xsl:if test="@isTestOnly = 'false'">
			<xsl:choose>
				<xsl:when test="@equals">
			<xsl:choose>
			<xsl:when test="@var">
			<xsl:if test="@type != 'null'">
<xsl:value-of select="@type" /> <xsl:text> 
			</xsl:text> <xsl:value-of select="@var" /> &lt;- </xsl:if><xsl:value-of select="@code" /><xsl:if test="@type != 'null'">; </xsl:if>//<xsl:value-of select="@var" /> equals <xsl:value-of select="@equals" /><xsl:text></xsl:text>
			</xsl:when>
			<xsl:otherwise><xsl:text> 
			</xsl:text>
<xsl:value-of select="@type" /> var<xsl:value-of select="@index" /> &lt;- <xsl:value-of select="@code" />; // var<xsl:value-of select="@index" /> equals <xsl:value-of select="@equals" /><xsl:text></xsl:text>
			</xsl:otherwise>
			
			</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
<xsl:value-of select="@code" /> <xsl:text> 
</xsl:text>
			</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:for-each>
</xsl:template>


<xsl:template name="checkName">
<xsl:choose>
<xsl:when test="@name = '*'">
<xsl:text>`*`</xsl:text>
</xsl:when>
<xsl:when test="@name = '**'">
<xsl:text>`**`</xsl:text>
</xsl:when>
<xsl:when test="@name = '&lt;-&gt;'">
<xsl:text>`&lt;-&gt;`</xsl:text>
</xsl:when>
<xsl:when test="@name = '='">
<xsl:text>`=`</xsl:text>
</xsl:when>
<xsl:otherwise>
<xsl:text>`</xsl:text>
<xsl:value-of select="@name" />
<xsl:text>`</xsl:text>
</xsl:otherwise>
</xsl:choose>
</xsl:template>


<xsl:template name="msgIntro">
<xsl:text>

----

**This file is automatically generated from java files. Do Not Edit It.**

----

</xsl:text>
</xsl:template>


<xsl:template name="keyword">
	<xsl:param name="category" />
	<xsl:param name="nameGAMLElement" />
	<xsl:text>
	[//]: # (keyword|</xsl:text>
	<xsl:value-of select="$category" />
	<xsl:text>_</xsl:text>
	<xsl:value-of select="$nameGAMLElement" />
	<xsl:text>)</xsl:text>
</xsl:template>



    
<xsl:template name="buildVariables"> 

### Variables

	<xsl:for-each select="vars/var">		
	<xsl:sort select="@name" />   
  * **`<xsl:value-of select="@name"/>`** (`<xsl:value-of select="@type"/>`): <xsl:value-of select="documentation/result"/> 
	</xsl:for-each>
</xsl:template>


<xsl:template name="buildActions"> 
 	
### Actions
	<xsl:for-each select="actions/action">		
	<xsl:sort select="@name" />  
	 
#### **`<xsl:value-of select="@name"/>`**
	<xsl:value-of select="documentation/result"/><xsl:text>
	</xsl:text>
	
* returns: <xsl:value-of select="@returnType"/>
  	<xsl:for-each select="args/arg"> 			
* **`<xsl:value-of select="@name"/>`** (<xsl:value-of select="@type"/>): <xsl:value-of select="documentation/result"/> 
  	</xsl:for-each>
		
	<xsl:if test="documentation/examples[node()]">

```

<xsl:for-each select="documentation/examples/example" >
<xsl:if test="@code != ''"><xsl:value-of select="@code"/><xsl:text>
</xsl:text>
</xsl:if>
</xsl:for-each>```
</xsl:if>	
		
		</xsl:for-each>	
</xsl:template>

</xsl:stylesheet>
