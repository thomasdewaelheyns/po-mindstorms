TARGET=verslag.pdf

all: ${TARGET}

clean:
	@rm -f *.log *.aux *.toc *.gz

%.pdf: %.tex
	@pdflatex $<
	@touch $<
	@pdflatex $<	

dist-clean: clean
	@rm -f ${TARGET}
