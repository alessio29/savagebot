grammar R2;

// To generate parser, run
//    mvn generate-sources
// or antlr4:antlr4 goal

statement
    :   e=expression
        # RollOnceStmt
    |   n=term ('x'|'X') e=expression
        # RollTimesStmt
    ;

expression
    :   genericRoll
        # GenericRollExpr
    |   savageWorldsRoll
        # SavageWorldsRollExpr
    |   fudgeRoll
        # FudgeRollExpr
    |   e1=expression op=('*'|'/'|'%') e2=expression
        # InfixExpr1
    |   e1=expression op=('+'|'-') e2=expression
        # InfixExpr2
    |   op=('+'|'-') e1=expression
        # PrefixExpr
    |   t=term
        # TermExpr
    ;

genericRoll
    :   (t1=term)? ('d'|'D') t2=term (excl='!')? genericRollSuffix?
    ;

genericRollSuffix
    :   op=('k'|'K'|'kl'|'KL'|'a'|'d'|'adv'|'dis') (n=term)?
    ;

savageWorldsRoll
    :   (t1=term)? ('s'|'S') t2=term (('w'|'W') t3=term)?
    ;

fudgeRoll
    :   (t=term)? ('dF'|'df'|'DF')
    ;

term
    :   i=INT
        # IntTerm
    |   '(' e=expression ')'
        # ExprTerm
    ;

INT: [0-9]+;
WS: [ \t\n\r] -> skip;