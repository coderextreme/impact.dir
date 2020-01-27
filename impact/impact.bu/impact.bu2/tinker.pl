#!/usr/bin/perl -ws

# Tinker-High syntax parser (no semantics)

# Makes use of the Parse::RecDescent available from CPAN

# I'm not really sure this is parsing, try tweaking $mod
# to see if it finds syntax errors.  In particular, the
# Module line can end with a : or a ; ???????????????

use Parse::RecDescent;

# heres the grammar

$grammar =
q{
	mains : main mains
		| main
	main : 'Module' rvar ':'
		decls
		'begin'
			exprs
		'end' ';'
	type : '<' 'complex' '>'
		| '<' 'integer' '>'
		| '<' 'real' '>'
		| '<' 'boolean' '>'
		| '<' rvar '>'
	decls : decl decls
		| decl
		| //
	decl : inputs
		| outputs
		| moduledecls
	inputs : input inputs
		| input
	input :
		 'Input' type rvars ';'
	outputs : output outputs
		| output
	output :
		 'Output' type lvars ';'
	moduledecls : 'Uses' modules ';'
	modules : rvar ',' modules
		| rvar
	rvars : rvar ',' rvars
		| rvar
	lvars : lvar ',' lvars
		| lvar
	exprs : expr exprs
		| expr
	expr :	lvar '=' addition ';'
	addition : <leftop:multiplication add_op multiplication>
	multiplication : <leftop:exponentiation mult_op exponentiation>
	exponentiation : <leftop:factor exp_op factor>
	factor : number
		| rvar
		| '-' rvar
		| '(' addition ')'
		| '&' rvar '(' addition ')'
        add_op: '+'
              | '-'
        mult_op: '*'
               | '/'
	exp_op : '**'
        number: /[-+]?\d+(\.\d+)?/
        lvar:   /([a-zA-Z_][a-zA-Z_0-9]*)/
		| /([a-zA-Z_][a-zA-Z_0-9]*)/ '[' lvar ']'
	rvar : lvar
};

# here we create the parser
$parse = new Parse::RecDescent($grammar) or die "bad grammar";

# here is the sample module
$mod = "
Module AND:

Input <boolean> A, B;
Output <boolean> Out;
Uses <NAND> NAND1, NAND2;
Uses <Replicate> Copy;

NAND1[A] = A;
NAND1[B] = B;
Copy[In] = NAND1[Out];

NAND2[A] = Copy[OutA];
NAND2[B] = Copy[OutB];

Out = NAND2[Out];

end;

Module quadratic:

Input <complex> a, b, c;
Output <complex> x1, x2;
Uses squareroot;

begin

x1 = (-b + &squareroot(b ** 2 - 4 * a * c)) / (2 * a);
x2 = (-b - &squareroot(b ** 2 - 4 * a * c)) / (2 * a); 

end;

Module add:

Input <real> a, b;
Output <real> sum;

begin
	sum = a + b;
end;

Module null:
begin
end;
";
# here is where we parse the sample module
$parse->mains($mod);
