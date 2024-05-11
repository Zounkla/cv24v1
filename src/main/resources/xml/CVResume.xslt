<xsl:stylesheet version="3.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="cvs">
        <xsl:element name="html">
            <xsl:element name="head">
                <xsl:element name="title">CV</xsl:element>
                <link href="/cv24.css" rel="stylesheet"/>
            </xsl:element>
            <xsl:element name="body">
                <h1>Liste des CV</h1>
                <xsl:for-each select="cv">
                    <xsl:call-template name="createId"/>
                    <xsl:call-template name="createIdentity"/>
                    <xsl:call-template name="createObjective"/>
                    <xsl:call-template name="createDiploma"/>
                </xsl:for-each>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    <xsl:template name="createIdentity">
        <xsl:element name="h3">
            <xsl:value-of select="identite/genre"/>&#160;<xsl:value-of select="identite/nom"/>&#160;<xsl:value-of select="identite/prenom"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="createId">
        <xsl:element name="h2">
            <xsl:text>CV n°</xsl:text><xsl:value-of select="id"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="createObjective">
        <xsl:text>Objectif : </xsl:text>
        <xsl:element name="h3">
            <xsl:value-of select="objectif"/>
        </xsl:element>
        <xsl:element name="p">
            <xsl:text>Demande de </xsl:text><xsl:value-of select="objectif/@statut"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="createDiploma">
        <xsl:element name="h3">
            <xsl:text>Diplôme :</xsl:text>
        </xsl:element>
        <xsl:call-template name="transformDate">
            <xsl:with-param name="date" select="diplome/date"/>
        </xsl:call-template>
        <xsl:text> - </xsl:text>
        <xsl:value-of select="diplome/titre"/>
        <xsl:text> (niveau </xsl:text><xsl:value-of select="diplome/@niveau"/> <xsl:text>)</xsl:text>
        <xsl:text> </xsl:text><xsl:value-of select="diplome/institut"/>
    </xsl:template>
    <xsl:template name="transformDate">
        <xsl:param name="date"/>
        <xsl:variable name="day" select="substring($date, 9, 2)"/>
        <xsl:variable name="year" select="substring($date, 1, 4)"/>
        <xsl:variable name="month" select="substring($date, 6, 2)"/>

        <xsl:value-of select="$day"/>
        <xsl:value-of select="' '"/>

        <xsl:choose>
            <xsl:when test="$month = '01'">Janv</xsl:when>
            <xsl:when test="$month = '02'">Fevr</xsl:when>
            <xsl:when test="$month = '03'">Mars</xsl:when>
            <xsl:when test="$month = '04'">Avr</xsl:when>
            <xsl:when test="$month = '05'">Mai</xsl:when>
            <xsl:when test="$month = '06'">Juin</xsl:when>
            <xsl:when test="$month = '07'">Juil</xsl:when>
            <xsl:when test="$month = '08'">Aout</xsl:when>
            <xsl:when test="$month = '09'">Sept</xsl:when>
            <xsl:when test="$month = '10'">Oct</xsl:when>
            <xsl:when test="$month = '11'">Nov</xsl:when>
            <xsl:when test="$month = '12'">Dec</xsl:when>
        </xsl:choose>
        <xsl:value-of select="' '"/>
        <xsl:value-of select="$year"/>
    </xsl:template>
</xsl:stylesheet>