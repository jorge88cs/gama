<?xml version="1.0" encoding="UTF-8"?><!---->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:wiki="www.google.fr">

<xsl:variable name="html_menu" select="''"/>
<xsl:variable name="html_doc" select="''"/>
<xsl:variable name="html_iterator" select="0"/>
<xsl:variable name="emptyStr" select="''"/>

<xsl:variable name="alphabetID" select="'lz'"/>
<xsl:variable name="fileAK" select="'OperatorsAK161'"/>
<xsl:variable name="fileLZ" select="'OperatorsLZ161'"/>

<xsl:template match="/">
 	<xsl:text>#summary Operators(Modeling Guide)
 	
&lt;font color="red"&gt; This file is automatically generated from java files. Do Not Edit It. &lt;/font&gt;

= &lt;font color="blue"&gt; Table of Contents &lt;/font&gt; =

&lt;wiki:toc max_depth="1" /&gt;

= &lt;font color="blue"&gt; Operators by categories &lt;/font&gt; = 
</xsl:text>
	<xsl:call-template name="buildOperatorsByCategories"/>
<xsl:text>

= &lt;font color="blue"&gt; Operators by alphabetic order &lt;/font&gt; =

&lt;wiki:toc max_depth="2" /&gt;

= &lt;font color="blue"&gt; Definition &lt;/font&gt; =

An operator performs a function on one, two, or three operands. An operator that only requires one operand is called a unary operator. An operator that requires two operands is a binary operator. And finally, a ternary operator is one that requires three operands. The GAML programming language has only one ternary operator, ? :, which is a short-hand if-else statement.

Unary operators are written using aprefix parenthesized notation. Prefix notation means that the operator appears before its operand. Note that unary expressions should always been parenthesized:

{{{
unary_operator (operand)
}}}

Most of binary operators can use two notations:
  * the fonctional notation, which used a parenthesized notation around the operands (this notation cannot be used with arithmetic and relational operators such as: +, -, /, `*`, ^, =, !=, &lt;, &gt;, &gt;=, &lt;=... )
  * the infix notation, which means that the operator appears between its operands

{{{
binary_operator(op1, op2)

Or 

op1 binary_operator op2    
}}}

The ternary operator is also infix; each component of the operator appears between operands:

{{{
op1 ? op2 : op3
}}}

In addition to performing operations, operators are functional, i.e. they return a value. The return value and its type depend on the operator and the type of its operands. For example, the arithmetic operators, which perform basic arithmetic operations such as addition and subtraction, return numbers - the result of the arithmetic operation.

Moreover, operators are strictly functional, i.e. they have no side effects on their operands. For instance, the shuffle operator, which randomizes the positions of elements in a list, does not modify its list operand but returns a new shuffled list.

[#Table_of_Contents Top of the page] 
	</xsl:text>

= &lt;font color="blue"&gt; Operators &lt;/font&gt; =
	<xsl:call-template name="buildOperators"/>

</xsl:template>


 
<xsl:template name="buildOperatorsByCategories">
	<xsl:for-each select="doc/operatorsCategories/category">
		<xsl:sort select="@id"/>
		<xsl:variable name="categoryGlobal" select="@id"/> 
		<xsl:text>
=== </xsl:text> <xsl:value-of select="@id"/> <xsl:text> ===</xsl:text>
		<xsl:text>
  * </xsl:text>
		<xsl:for-each select="/doc/operators/operator"> 
			<xsl:sort select="@name" />
				<xsl:variable name="nameOp" select="@name"/>
			
			<xsl:for-each select="operatorCategories/category">
				<xsl:variable name="catItem" select="@id"/>
				<xsl:if test="$catItem = $categoryGlobal "> 
					 <xsl:for-each select="/doc/operators/operator[@id = $nameOp]">
					 	<xsl:choose>
							<xsl:when test="@alphabetOrder = 'ak'"> 
								<xsl:text>[</xsl:text><xsl:value-of select="$fileAK"/><xsl:text>#</xsl:text><xsl:value-of select="$nameOp"/><xsl:text> </xsl:text><xsl:value-of select="$nameOp"/><xsl:text>], </xsl:text> 
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>[</xsl:text><xsl:value-of select="$fileLZ"/><xsl:text>#</xsl:text><xsl:value-of select="$nameOp"/><xsl:text> </xsl:text><xsl:value-of select="$nameOp"/><xsl:text>], </xsl:text> 
							</xsl:otherwise>
						</xsl:choose>
					 </xsl:for-each> 
				</xsl:if>			
			</xsl:for-each>
		</xsl:for-each>    	
	</xsl:for-each>
</xsl:template>
    
 <xsl:template name="buildOperators"> 
    <xsl:for-each select="doc/operators/operator[@alphabetOrder = $alphabetID]">
    	<xsl:sort select="@name" />
    
== <xsl:call-template name="checkName"/> == 
  	<xsl:if test="@alternativeNameOf">
 Same signification as [#<xsl:value-of select="@alternativeNameOf"/><xsl:text> </xsl:text><xsl:value-of select="@alternativeNameOf"/>] operator.
  	</xsl:if>
  	
  	<xsl:if test="combinaisonIO[node()]">
		<xsl:call-template name="buildOperands"/>
	</xsl:if>
	
	<xsl:if test="documentation/result[text()]"> 
  * Result: <xsl:value-of select="documentation/result"/>
    </xsl:if>
    
  <xsl:if test="documentation/comment[text()]">  
  * Comment: <xsl:value-of select="documentation/comment"/> 
  </xsl:if>
  
  <xsl:if test="documentation/specialCases[node()]">
  * Special cases:<xsl:for-each select="documentation/specialCases/case">
    * <xsl:value-of select="@item"/> </xsl:for-each>
  </xsl:if>  

  <xsl:if test="documentation/usages[node()] | documentation/usagesNoExample[node()]">
  * Special cases from usages: <xsl:for-each select="documentation/usagesNoExample/usage">
    * <xsl:value-of select="@descUsageElt"/> </xsl:for-each>
  <xsl:for-each select="documentation/usages/usage">
    * <xsl:value-of select="@descUsageElt"/> 
  {{{
<xsl:call-template name="buildExamples"/> }}} </xsl:for-each>
  </xsl:if>

  <xsl:if test="documentation/usagesExamples[node()]">
  * Examples from usages: 
  {{{ 
<xsl:for-each select="documentation/usagesExamples/usage">
<xsl:call-template name="buildExamples"/> </xsl:for-each>}}} 
  </xsl:if>
  
  <xsl:if test="documentation/seeAlso[node()]">    
  * See also: <xsl:for-each select="documentation/seeAlso/see">
 	<xsl:variable name="idOperator" select="@id"/>
 <xsl:for-each select="/doc/operators/operator[@id = $idOperator]">
 	<xsl:choose>
		<xsl:when test="@alphabetOrder = 'ak'"> 
			<xsl:text>[</xsl:text><xsl:value-of select="$fileAK"/><xsl:text>#</xsl:text><xsl:value-of select="$idOperator"/><xsl:text> </xsl:text><xsl:value-of select="$idOperator"/><xsl:text>], </xsl:text> 
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>[</xsl:text><xsl:value-of select="$fileLZ"/><xsl:text>#</xsl:text><xsl:value-of select="$idOperator"/><xsl:text> </xsl:text><xsl:value-of select="$idOperator"/><xsl:text>], </xsl:text> 
		</xsl:otherwise>
	</xsl:choose>
 </xsl:for-each> 
  
  </xsl:for-each>

  </xsl:if>
  
  <xsl:if test="documentation/examples[node()]">
  
{{{
<xsl:for-each select="documentation/examples/example" >
<xsl:if test="@code != ''"><xsl:value-of select="@code"/><xsl:text>
</xsl:text>
</xsl:if>
</xsl:for-each> }}} 
</xsl:if>
[#Table_of_Contents Top of the page] 
  	</xsl:for-each>
 </xsl:template>   
 
 <xsl:template name="buildOperands">
  * Possible use: <xsl:for-each select="combinaisonIO/operands"> <xsl:sort select="count(operand)"/> <xsl:call-template name="buildOperand"/> </xsl:for-each>
 </xsl:template> 
 
 <xsl:template name="buildOperand">
	<xsl:choose>
	<xsl:when test="count(operand) = 1">
    * OP(<xsl:value-of select="operand/@type"/>) --->  <xsl:value-of select="@returnType"/> 
	</xsl:when>
	<xsl:otherwise>
    * <xsl:value-of select="operand[@position=0]/@type"/> <xsl:text> OP </xsl:text> <xsl:value-of select="operand[@position=1]/@type"/> --->  <xsl:value-of select="@returnType"/>	
	</xsl:otherwise>
	</xsl:choose>
 </xsl:template> 
 
 <xsl:template name="buildExamples">
	<xsl:for-each select="examples/example">
	  	<xsl:if test="@isTestOnly = 'false'">	
		<xsl:choose>
			<xsl:when test="@equals">
				<xsl:value-of select="@type"/> var<xsl:value-of select="@index"/> &lt;- <xsl:value-of select="@code"/>; 	// var<xsl:value-of select="@index"/> equals <xsl:value-of select="@equals"/><xsl:text>
</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="@code"/><xsl:text>
</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		</xsl:if>
	</xsl:for-each>
</xsl:template>	
 
 <xsl:template name="checkName">
 	<xsl:choose>
 		<xsl:when test="@name = '*'"><xsl:text>`*`</xsl:text></xsl:when>
 		<xsl:when test="@name = '**'"><xsl:text>`**`</xsl:text></xsl:when>
 		<xsl:when test="@name = '&lt;-&gt;'"><xsl:text>`&lt;-&gt;`</xsl:text></xsl:when> 		
 		<xsl:otherwise><xsl:value-of select="@name"/></xsl:otherwise>
 	</xsl:choose>
 </xsl:template>

 <xsl:template name="linkName">
 	<xsl:choose>
 		<xsl:when test="@name = '*'"><xsl:text>`*`</xsl:text></xsl:when>
 		<xsl:otherwise><xsl:value-of select="@name"/></xsl:otherwise>
 	</xsl:choose>
 </xsl:template>
 
</xsl:stylesheet>

<!--  
<xsl:for-each select="documentation/usages/usage" >  
<xsl:call-template name="generateTestFromExample"/>  
</xsl:for-each>
<xsl:for-each select="documentation/usagesExamples/usage" >  
<xsl:call-template name="generateTestFromExample"/>  
</xsl:for-each>
-->

