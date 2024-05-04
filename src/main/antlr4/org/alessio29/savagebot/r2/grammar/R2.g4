grammar R2;

// To generate parser, run
//    mvn generate-sources
// or antlr4:antlr4 goal

commandElement
    :   statement (';' statement)* ';'? EOF
    ;

statement
    :   e=expression                                    # RollOnceStmt
    |   n=term ('x'|'X') e=expression                   # RollTimesStmt
    |   n=term ('x'|'X') '[' batchElement* ']'          # RollBatchTimesStmt
    |   // Hack to support short-hand repetition for Savage Worlds extras roll:
        // '4e6' is desugared to '4xe6', so it's effectively a repeat statement.
        n=term ('e'|'E') t1=term targetNumberAndRaiseStep? additiveModifier?
                                                        # RollSavageWorldsExtraStmt
    |   ('i'|'I') additiveModifier?                     # IronSwornRollStmt
    |   flag=FLAG                                       # FlagStmt
    ;

batchElement
    :   (comment=STRING)? e=expression ';'?
    ;

expression
    :   genericRoll                                     # GenericRollExpr
    |   savageWorldsRoll                                # SavageWorldsRollExpr
    |   savageWorldsExtrasRoll                          # SavageWorldsExtrasRollExpr
    |   fudgeRoll                                       # FudgeRollExpr
    |   carcosaRoll                                     # CarcosaRollExpr
    |   wegD6Roll                                       # WegD6RollExpr
    |   swordWorldPowerRoll                             # SwordWorldPowerRollExpr
    |   e1=expression
        '[' (e2=expression)? ':' (e3=expression)? ']'   # BoundedExpr
    |   targetNumberAndRaiseStep ':' e1=expression      # TargetNumberAndRaiseStepExpr
    |   v=VAR ':=' e1=expression                        # AssignExpr
    |   e1=expression op=('*'|'/'|'%') e2=expression    # InfixExpr1
    |   g0=INT '--' g1=INT                              # GygaxRangeRollExpr
    |   e1=expression op=('+'|'-') e2=expression        # InfixExpr2
    |   op=('+'|'-') e1=expression                      # PrefixExpr
    |   t=term                                          # TermExpr
    ;

genericRoll
    :   (t1=term)? ('d'|'D') (t2=dieFacetsTerm) (excl='!')? genericRollSuffix?
    ;

dieFacetsTerm: term | '%';

genericRollSuffix
    :   op=('k'|'K'|'kl'|'KL'|'adv'|'dis') (n=term)?    # RollAndKeepSuffix
    |   sop=('s'|'S') sn=term (fop=('f'|'F') fn=term)?  # SuccessOrFailSuffix1
    |   fop=('f'|'F') fn=term sop=('s'|'S') sn=term     # SuccessOrFailSuffix2
    |   targetNumberAndRaiseStep                        # TargetNumberAndRaiseStepSuffix
    ;

savageWorldsRoll
    :   (t1=term)? ('s'|'S') t2=term (('w'|'W') t3=term)?
        targetNumberAndRaiseStep?
    ;

savageWorldsExtrasRoll
    :   ('e'|'E') t1=term targetNumberAndRaiseStep?
    ;

swordWorldPowerRoll
    :   ('p'|'P') tp=term swordWorldPowerRollModifier*
    ;

swordWorldPowerRollModifier
    :   ('c'|'C') tc=term               # SwordWorldCriticalModifier
    |   ('f'|'F') tf=term               # SwordWorldAutoFailModifier
    |   dop=('h'|'H')                   # SwordWorldHumanSwordGraceModifier
    |   '['
        ((td=term)? dop=('d'|'D'))?
        (mop=('+'|'-') tm=term)?
        ']'                             # SwordWorldRollModifier
    ;

targetNumberAndRaiseStep
    :   ('tr'|'TR') tnr=term
    |   ('t'|'T') tt=term (('r'|'R') tr=term)?
    |   ('r'|'R') tr=term (('t'|'T') tt=term)?
    |   ('tn'|'TN') tgtn=term ('+'|'-')?
    ;

additiveModifier
    :   op=('+'|'-') em=expression
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
    :   i=INT                                       # IntTerm
    |   v=VAR                                       # VarTerm
    |   '(' (comment=STRING)? e=expression ')'      # ExprTerm
    ;

INT: [0-9]+;
STRING: '"' StringChar* '"';
WS: [ \t\n\r] -> skip;
FLAG: '--'[a-zA-Z]+;
VAR: '@'[a-zA-Z0-9_]+;

fragment StringChar: ~["\\\r\n];