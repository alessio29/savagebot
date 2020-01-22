grammar R2;

// To generate parser, run
//    mvn generate-sources
// or antlr4:antlr4 goal

commandElement
    :   statement (';' statement)* ';'? EOF
    ;

statement
    :   e=expression
        # RollOnceStmt
    |   n=term ('x'|'X') e=expression
        # RollTimesStmt
    |   n=term ('x'|'X') '[' batchElement* ']'
        # RollBatchTimesStmt
    |   flag=FLAG
        # FlagStmt
    ;

batchElement
    :   (comment=STRING)? e=expression ';'?
    ;

expression
    :   genericRoll
        # GenericRollExpr
    |   savageWorldsRoll
        # SavageWorldsRollExpr
    |   fudgeRoll
        # FudgeRollExpr
    |   carcosaRoll
        # CarcosaRollExpr
    |   wegD6Roll
        # WegD6RollExpr
    |   e1=expression '[' (e2=expression)? ':' (e3=expression)? ']'
        # BoundedExpr
    |   v=VAR ':=' e1=expression
        # AssignExpr
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
    :   (t1=term)? ('d'|'D') (t2=dieFacetsTerm) (excl='!')? genericRollSuffix?
    ;

dieFacetsTerm: term | '%';

genericRollSuffix
    :   op=('k'|'K'|'kl'|'KL'|'adv'|'dis') (n=term)?
        # RollAndKeepSuffix
    |   sop=('s'|'S') sn=term (fop=('f'|'F') fn=term)?
        # SuccessOrFailSuffix1
    |   fop=('f'|'F') fn=term sop=('s'|'S') sn=term
        # SuccessOrFailSuffix2
    ;

savageWorldsRoll
    :   (t1=term)? ('s'|'S') t2=term (('w'|'W') t3=term)?
    ;

fudgeRoll
    :   (t=term)? ('dF'|'df'|'DF')
    ;

carcosaRoll
    :   (t=term)? ('dC'|'dc'|'DC')
    ;

wegD6Roll
    :   (t=term) ('w'|'W')
    ;

term
    :   i=INT
        # IntTerm
    |   v=VAR
        # VarTerm
    |   '(' (comment=STRING)? e=expression ')'
        # ExprTerm
    ;

INT: [0-9]+;
STRING: '"' StringChar* '"';
WS: [ \t\n\r] -> skip;
FLAG: '--'[a-zA-Z]+;
VAR: '@'[a-zA-Z0-9_]+;

fragment StringChar: ~["\\\r\n];