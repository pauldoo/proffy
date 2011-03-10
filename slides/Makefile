# MacPorts: port install docbook-xml docbook-xsl fop

all: proffy.pdf

DOCBOOK_FO_XSL := /opt/local/share/xsl/docbook-xsl/slides/fo/plain.xsl

%.pdf: %.xml
	fop -xml $< -xsl $(DOCBOOK_FO_XSL) -pdf $@
