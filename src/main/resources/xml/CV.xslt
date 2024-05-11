<xsl:stylesheet version="3.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cv24="http://univ.fr/cv24"
                xmlns:fn="http://www.w3.org/2005/xpath-functions">
    <xsl:template match="cv24:cv24">
        <xsl:element name="html">
            <xsl:element name="head">
                <xsl:element name="title">CV</xsl:element>
                <link href="/cv24.css" rel="stylesheet"/>
            </xsl:element>
            <xsl:element name="body">
                <h1>CV24 - XSLT V1.0</h1>
                <p>Le
                    <xsl:call-template name="transformDate">
                        <xsl:with-param name="date" select="string(fn:current-date())"/>
                    </xsl:call-template>
                </p>
                <xsl:call-template name="createIdentity"/>
                <xsl:call-template name="createObjective"/>
                <xsl:call-template name="createExp"/>
                <xsl:call-template name="createDiplome"/>
                <xsl:call-template name="createCertif"/>
                <xsl:call-template name="createLanguage"/>
                <xsl:call-template name="createDivers"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    <xsl:template name="createIdentity">
        <xsl:element name="h2">
            <xsl:value-of select="cv24:identite/cv24:genre"/>&#160;<xsl:value-of select="cv24:identite/cv24:nom"/>&#160;<xsl:value-of select="cv24:identite/cv24:prenom"/>
        </xsl:element>
        <xsl:element name="p">
            <xsl:text>Tel : </xsl:text>
            <xsl:call-template name="transformTel">
                <xsl:with-param name="tel" select="cv24:identite/cv24:tel"/>
            </xsl:call-template>
            <xsl:element name="br"/>
            <xsl:text>Mel : </xsl:text><xsl:value-of select="cv24:identite/cv24:mel"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="createObjective">
        <xsl:element name="h2">
            <xsl:value-of select="cv24:objectif"/>
        </xsl:element>
        <xsl:element name="p">
            <xsl:text>Demande de </xsl:text><xsl:value-of select="cv24:objectif/@statut"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="createExp">
        <xsl:element name="h2">
            <xsl:text>Expériences professionnelles</xsl:text>
        </xsl:element>
        <xsl:element name="ol">
            <xsl:for-each select="cv24:prof/cv24:detail">
                <xsl:element name="li">
                    <xsl:value-of select="cv24:titre"/>
                    <xsl:if test="fn:exists(cv24:datefin)">
                        <xsl:text> ( du </xsl:text>
                        <xsl:call-template name="transformDate">
                            <xsl:with-param name="date" select="cv24:datedeb"/>
                        </xsl:call-template>
                        <xsl:text> au </xsl:text>
                        <xsl:call-template name="transformDate">
                            <xsl:with-param name="date" select="cv24:datefin"/>
                        </xsl:call-template>
                        <xsl:text>)</xsl:text>
                    </xsl:if>
                    <xsl:if test="not(fn:exists(cv24:datefin))">
                        <xsl:text> ( depuis le </xsl:text>
                        <xsl:call-template name="transformDate">
                            <xsl:with-param name="date" select="cv24:datedeb"/>
                        </xsl:call-template>
                        <xsl:text>)</xsl:text>
                    </xsl:if>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>
    <xsl:template name="createDiplome">
        <xsl:element name="h2">
            <xsl:text>Diplômes</xsl:text>
        </xsl:element>
        <xsl:element name="table">
            <xsl:for-each select="cv24:competences/cv24:diplome" >
                <xsl:sort select="@niveau" order="descending"/>
                <xsl:element name="tr">
                    <xsl:element name="td">
                        <xsl:call-template name="transformDate">
                            <xsl:with-param name="date" select="cv24:date"/>
                        </xsl:call-template>
                    </xsl:element>
                    <xsl:element name="td">
                        <xsl:value-of select="cv24:titre"/>
                    </xsl:element>
                    <xsl:element name="td">
                        <xsl:text> (niveau </xsl:text><xsl:value-of select="@niveau"/> <xsl:text>)</xsl:text>
                    </xsl:element>
                    <xsl:element name="td">
                        <xsl:text> </xsl:text><xsl:value-of select="cv24:institut"/>
                    </xsl:element>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>
    <xsl:template name="createCertif">
        <xsl:element name="h2">
            <xsl:value-of select="fn:count(//cv24:competences/cv24:certif)"/>
            <xsl:text> </xsl:text>
            <xsl:text>Certification(s)</xsl:text>
        </xsl:element>
        <xsl:element name="table">
            <xsl:for-each select="cv24:competences/cv24:certif">
                <xsl:element name="tr">
                    <xsl:element name="td">
                        <xsl:call-template name="transformDate">
                            <xsl:with-param name="date" select="cv24:datedeb"/>
                        </xsl:call-template>
                        <xsl:text> - </xsl:text>
                        <xsl:call-template name="transformDate">
                            <xsl:with-param name="date" select="cv24:datefin"/>
                        </xsl:call-template>
                    </xsl:element>
                    <xsl:element name="td">
                        <xsl:value-of select="cv24:titre"/>
                    </xsl:element>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

    <xsl:template name="createLanguage">
        <xsl:element name="h2">
            <xsl:text>Langues</xsl:text>
        </xsl:element>
        <xsl:element name="ul">
            <xsl:for-each select="cv24:divers/cv24:lv">
                <xsl:element name="li">
                    <xsl:value-of select="@lang"/>
                    <xsl:text> : </xsl:text>
                    <xsl:value-of select="@cert"/>
                    <xsl:text> (TOEIC : </xsl:text>
                    <xsl:value-of select="@nivi"/>, CLES : <xsl:value-of
                        select="@nivs"/><xsl:text>)</xsl:text>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

    <xsl:template name="createDivers">
        <xsl:element name="h2">
            <xsl:text>Divers</xsl:text>
        </xsl:element>
        <xsl:element name="ul">
            <xsl:for-each select="cv24:divers/cv24:autre">
                <xsl:element name="li">
                    <xsl:value-of select="@titre"/>
                    <xsl:text> : </xsl:text>
                    <xsl:value-of select="@comment"/>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
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

    <xsl:template name="transformTel">
        <xsl:param name="tel"/>
        <xsl:variable name="isIndicatif" select="substring($tel, 1, 3)"/>
        <xsl:variable name="reste" select="substring-after($tel, $isIndicatif)"/>
        <xsl:choose>
            <xsl:when test="$isIndicatif = '+33'">+33 (0)<xsl:value-of select="$reste"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="$tel"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>