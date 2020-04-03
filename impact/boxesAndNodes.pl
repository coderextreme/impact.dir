#!/usr/bin/perl -ws

use Parse::RecDescent;

$::RD_AUTOACTION = q { [@item] };

$grammar =
q{
	startrule : decl startrule
		| decl
	decl : module
		| connection
	module : outputs '=' rvar '(' inputs ')'
		'{'
			exprs
		'}' ';'
	connection : output '=' input
	type : 'complex'
		| 'integer'
		| 'real'
		| 'boolean'
		| 'string'
		| 'module'
	inputs : input ',' inputs
		| input
	input : type rvar
	outputs : output ',' outputs
		| output
	output : type lvar
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
integer D, integer E = Output(integer B, integer C) {
	D = B * C;
	E = C - B;
}


integer F, integer G = ModuleB() {
	F = 6
	G = 3
}

integer B = integer G
integer C = integer F


";
# here is where we parse the sample module
print $parse->startrule($mod);
