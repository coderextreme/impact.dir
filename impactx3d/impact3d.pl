#!/usr/bin/perl

print "Content-type: application/x-java-jnlp-file\n\n";

open (JNLP, "impact3d.jnlp");

while (<JNLP>) {
	print;
}
close(JNLP);
