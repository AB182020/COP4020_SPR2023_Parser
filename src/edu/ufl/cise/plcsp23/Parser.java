package edu.ufl.cise.plcsp23;

import edu.ufl.cise.plcsp23.ast.*;

import java.util.Arrays;

public class Parser implements IParser
{

    AST astObj;
    String lexInput;
    IToken.Kind preOp = null;
    String inputParser;
    final char[] inputParserChars;
    IToken.Kind kind;
    IToken nextToken;
    int tempPos;
    boolean condFlag = false;
    boolean parenFlag = false;
    Expr rightE;

    ConditionalExpr conditionalE;
    Expr leftE;

    Expr leftBinaryExp= null;
    String lexTemp;
    Expr e = null;
    RandomExpr rnd;
    IdentExpr idnt;
    ZExpr z;
    NumLitExpr numLit;
    StringLitExpr stringLit;
    UnaryExpr unary;
    BinaryExpr binary;

    Expr gaurdE;
    Expr trueCase;
    Expr falseCase;
    IScanner scanner;
    int currentPos =0;
    IToken firstToken;


    public Parser(String inputParser)
    {
        this.inputParser = inputParser;
        inputParserChars = Arrays.copyOf(inputParser.toCharArray(),inputParser.length()+1);
    }
    /*
     **Function for expression productions
     * <expr> ::= <conditional_expr> | <or_expr>
     */
    public Expr expr() throws PLCException
    {
        kind = firstToken.getKind();
        if(kind != IToken.Kind.RES_if)
        {
            leftE = orExpr();
        }
        else
        {
            leftE =  conditionalExpr();
        }

        return leftE;
    }
    public IToken consume() throws SyntaxException, LexicalException {

        while(inputParserChars[currentPos] == ' ')
        {
            currentPos++;
        }
        if(firstToken.getSourceLocation() != null && firstToken.getKind() != IToken.Kind.EOF)
        {

            //  currentPos =  currentPos+firstToken.getSourceLocation().column();
            currentPos =  currentPos + ((Token) firstToken).getLength();
        }
        else
        {
            currentPos = currentPos+1;
        }
        if(nextToken!= null)
        {
            if(nextToken.getKind() == IToken.Kind.OR || nextToken.getKind() == IToken.Kind.AND || nextToken.getKind() == IToken.Kind.EXP || nextToken.getKind()== IToken.Kind.LE || nextToken.getKind()== IToken.Kind.GE || nextToken.getKind()== IToken.Kind.EXP || nextToken.getKind()== IToken.Kind.EQ)
                currentPos = currentPos+1;
        }
        lexTemp = inputParser.substring(currentPos,inputParser.length());
        scanner = CompilerComponentFactory.makeScanner(lexTemp);
        IToken token;
        token = scanner.next();
        kind = token.getKind();
//       currentPos = currentPos+ token.getTokenString().length();
        return token;

    }
    public Expr primaryExpr() throws PLCException {
        IToken  currentToken;

        if(nextToken == null)
        {
            currentToken = firstToken;
        }

        else
        {
            currentToken = nextToken;
            if(condFlag != true && parenFlag != true &&currentToken.getKind() != IToken.Kind.EOF)
            {

                nextToken = consume();
            }

        }


        if(currentToken.getKind() == IToken.Kind.NUM_LIT)
        {
            numLit = new NumLitExpr(currentToken);
            if(nextToken == null)
            {
                nextToken = consume();
            }
            IToken.Kind eofKind = null;
            if(nextToken != null)
            {
                eofKind = nextToken.getKind();
            }
            if(eofKind != IToken.Kind.EOF)
                nextToken = consume();
            if(nextToken.getKind() == IToken.Kind.EOF || parenFlag == true)
                return numLit;
                //arithmetic
            else if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD|| nextToken.getKind() == IToken.Kind.EXP)
            {
                if(leftBinaryExp == null)
                    leftBinaryExp = numLit;
                else
                {
                    binary = new BinaryExpr(currentToken,leftBinaryExp,preOp,numLit);
                    leftBinaryExp = binary;
                }
                preOp = nextToken.getKind();
                nextToken = consume();
                if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD)
                    throw new SyntaxException("Invalid Op");
                else
                {

                    rightE = expr();
                    binary = new BinaryExpr(currentToken,leftBinaryExp,preOp,rightE);
                    return binary;
                }


            }
            //Logical
            else if(nextToken.getKind() == IToken.Kind.BITOR || nextToken.getKind() == IToken.Kind.OR || nextToken.getKind() == IToken.Kind.BITAND || nextToken.getKind() == IToken.Kind.AND)
            {
                leftBinaryExp = numLit;
                IToken.Kind op = nextToken.getKind();
                nextToken = consume();
                if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD)
                    throw new SyntaxException("Invalid Op");
                else
                {
                    rightE = expr();
                    binary = new BinaryExpr(currentToken,leftBinaryExp,op,rightE);
                    return binary;
                }

            }
            //relational
            else if(nextToken.getKind() == IToken.Kind.GE || nextToken.getKind() == IToken.Kind.GT || nextToken.getKind() == IToken.Kind.LE || nextToken.getKind() == IToken.Kind.LT || nextToken.getKind() == IToken.Kind.EQ)
            {
                leftBinaryExp = numLit;
                IToken.Kind op = nextToken.getKind();

                nextToken = consume();
                if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD)
                    throw new SyntaxException("Invalid Op");
                else
                {
                    rightE = expr();
                    binary = new BinaryExpr(currentToken,leftBinaryExp,op,rightE);
                    return binary;
                }

            }
            return numLit;
        }
        else if(currentToken.getKind() == IToken.Kind.STRING_LIT)
        {
            stringLit = new StringLitExpr(currentToken);
            return stringLit;
        }
        else if(currentToken.getKind() == IToken.Kind.RES_rand)
        {
            rnd = new RandomExpr(currentToken);
            return rnd;
        }
        // Z block starts
        else if(currentToken.getKind() == IToken.Kind.RES_Z)
        {
            z = new ZExpr(currentToken);
            IToken.Kind eofKind = null;
            if(nextToken != null)
            {
                eofKind = nextToken.getKind();
            }
            if(eofKind != IToken.Kind.EOF)
                nextToken = consume();
            if(nextToken.getKind() == IToken.Kind.QUESTION || nextToken.getKind() == IToken.Kind.EOF)
            {
                return z;
            }

            //binary arithmetic operation
            if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD || nextToken.getKind() == IToken.Kind.EXP)
            {
                leftBinaryExp = z;
                IToken.Kind op = nextToken.getKind();
                nextToken = consume();
                if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD)
                    throw new SyntaxException("Invalid Op");
                else
                {
                    rightE = expr();
                    binary = new BinaryExpr(currentToken,leftBinaryExp,op,rightE);
                    return binary;
                }



            }
            //LOGICAL
            else if(nextToken.getKind() == IToken.Kind.BITOR || nextToken.getKind() == IToken.Kind.OR || nextToken.getKind() == IToken.Kind.BITAND || nextToken.getKind() == IToken.Kind.AND)
            {
                leftBinaryExp = z;
                IToken.Kind op = nextToken.getKind();

                nextToken = consume();
                if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD)
                    throw new SyntaxException("Invalid Op");
                else
                {
                    rightE = expr();
                    binary = new BinaryExpr(currentToken,leftBinaryExp,op,rightE);
                    return binary;
                }

            }
            //relational operator
            else if(nextToken.getKind() == IToken.Kind.GE || nextToken.getKind() == IToken.Kind.GT || nextToken.getKind() == IToken.Kind.LE || nextToken.getKind() == IToken.Kind.LT|| nextToken.getKind() == IToken.Kind.EQ)
            {
                leftBinaryExp = z;
                IToken.Kind op = nextToken.getKind();

                nextToken = consume();
                if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD)
                    throw new SyntaxException("Invalid Op");
                else
                {
                    rightE = expr();
                    binary = new BinaryExpr(currentToken,leftBinaryExp,op,rightE);
                    return binary;
                }

            }
            return z;
        }
        else if(currentToken.getKind() == IToken.Kind.IDENT)
        {

            idnt = new IdentExpr(currentToken);
            IToken.Kind eofKind = null;
            if(nextToken != null)
            {
                eofKind = nextToken.getKind();
            }
            if(eofKind != IToken.Kind.EOF)
                nextToken = consume();
            if(nextToken.getKind() == IToken.Kind.QUESTION || nextToken.getKind() == IToken.Kind.EOF)
            {
                return idnt;
            }



            //binary arithmetic operation
            if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD|| nextToken.getKind() == IToken.Kind.EXP)
            {
                leftBinaryExp = idnt;
                IToken.Kind op = nextToken.getKind();
                nextToken = consume();
                if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD)
                    throw new SyntaxException("Invalid Op");
                else
                {
                    rightE = expr();
                    binary = new BinaryExpr(currentToken,leftBinaryExp,op,rightE);
                    return binary;
                }



            }
            //Logical Op
            else if(nextToken.getKind() == IToken.Kind.BITOR || nextToken.getKind() == IToken.Kind.OR || nextToken.getKind() == IToken.Kind.BITAND || nextToken.getKind() == IToken.Kind.AND)
            {
                leftBinaryExp = idnt;
                IToken.Kind op = nextToken.getKind();

                nextToken = consume();
                if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD)
                    throw new SyntaxException("Invalid Op");
                else
                {
                    rightE = expr();
                    binary = new BinaryExpr(currentToken,leftBinaryExp,op,rightE);
                    return binary;
                }

            }
            //relational
            else if(nextToken.getKind() == IToken.Kind.GE || nextToken.getKind() == IToken.Kind.GT || nextToken.getKind() == IToken.Kind.LE || nextToken.getKind() == IToken.Kind.LT|| nextToken.getKind() == IToken.Kind.EQ)
            {
                leftBinaryExp = idnt;
                IToken.Kind op = nextToken.getKind();

                nextToken = consume();
                if(nextToken.getKind() == IToken.Kind.PLUS || nextToken.getKind() == IToken.Kind.MINUS|| nextToken.getKind() == IToken.Kind.DIV|| nextToken.getKind() == IToken.Kind.TIMES|| nextToken.getKind() == IToken.Kind.MOD)
                    throw new SyntaxException("Invalid Op");
                else
                {
                    rightE = expr();
                    binary = new BinaryExpr(currentToken,leftBinaryExp,op,rightE);
                    return binary;
                }

            }
            return idnt;
        }


        else if((currentToken.getKind() == IToken.Kind.LPAREN || currentToken.getKind() == IToken.Kind.RPAREN))
        {
            parenFlag = true;
            if(currentToken.getKind() == IToken.Kind.RPAREN)
            {
                return e;
            }
            else
            {
                nextToken = consume();
                firstToken = nextToken;
                e =expr();
            }


//            if(nextToken.getKind() == IToken.Kind.RPAREN)
//            {
//               nextToken = consume();
//                if(nextToken.getKind() == IToken.Kind.EOF)
//                    return e;
//                //e = expr();
//
//            }


        }
        else
        {
            throw new SyntaxException("Unable to parse given expression");
        }



        return e;
    }

    public Expr unaryExpr() throws PLCException {

        kind = firstToken.getKind();
        if(kind == IToken.Kind.BANG || kind == IToken.Kind.RES_sin ||kind == IToken.Kind.RES_cos||kind == IToken.Kind.RES_atan||kind == IToken.Kind.MINUS )
        {
            IToken  currentToken = firstToken;
            nextToken =  consume();
            kind = nextToken.getKind();
            e = primaryExpr();
            unary = new UnaryExpr(currentToken,currentToken.getKind(),e);
            return unary;

        }
        else
            e =  primaryExpr();
        return e;
    }


    public Expr multiplicativeExpr() throws PLCException {
//        leftE = null;
//        rightE = null;
        kind = firstToken.getKind();
        leftE = unaryExpr();

        while(kind == IToken.Kind.TIMES ||  kind == IToken.Kind.DIV ||  kind == IToken.Kind.MOD)
        {
            consume();
            rightE = unaryExpr();
            // leftE =
        }
        return leftE;


    }
    public Expr additiveExpr() throws PLCException {
        kind = firstToken.getKind();
        leftE = multiplicativeExpr();

        while(kind == IToken.Kind.PLUS ||  kind == IToken.Kind.MINUS)
        {
            consume();
            rightE = multiplicativeExpr();
        }
        return leftE;
    }
    public Expr powerExpr() throws PLCException {
//        leftE = null;
//        rightE = null;
        kind = firstToken.getKind();
        leftE = additiveExpr();

        while(kind == IToken.Kind.EXP)
        {
            consume();
            rightE = additiveExpr();
            // leftE =
        }
        return leftE;
    }
    public Expr comparisonExpr() throws PLCException
    {
        kind = firstToken.getKind();
        leftE =powerExpr();

        while(kind == IToken.Kind.LT || kind == IToken.Kind.GT || kind == IToken.Kind.GE || kind == IToken.Kind.LE )
        {
            consume();
            rightE =powerExpr();
        }

        return leftE;
    }
    public Expr andExpr() throws PLCException
    {
        kind = firstToken.getKind();
        leftE =comparisonExpr();

        while(kind == IToken.Kind.AND ||  kind == IToken.Kind.BITAND)
        {
            consume();
            rightE =comparisonExpr();
        }

        return leftE;
    }

    public Expr orExpr() throws PLCException
    {
        kind = firstToken.getKind();
        leftE =andExpr();

        while(kind == IToken.Kind.OR ||  kind == IToken.Kind.BITOR)
        {
            consume();
            rightE =andExpr();
        }

        return leftE;
    }
    public Expr conditionalExpr() throws PLCException {
        condFlag = true;
        kind = firstToken.getKind();
        IToken currentToken = firstToken;
        if(kind == IToken.Kind.RES_if)
        {
            nextToken =  consume();
            gaurdE = primaryExpr();
//            if(nextToken.getKind() != IToken.Kind.QUESTION)
//            {
//                 firstToken = nextToken;
//                 nextToken = consume();
//
//            }
            firstToken = nextToken;
            nextToken = consume();
            trueCase = primaryExpr();
//            while(nextToken.getKind() != IToken.Kind.QUESTION)
//            {
//                firstToken = nextToken;
//                nextToken = consume();
//
//            }
            firstToken = nextToken;
            nextToken = consume();
            falseCase = primaryExpr();
            //  kind = nextToken.getKind();
            conditionalE = new ConditionalExpr(currentToken,gaurdE,trueCase,falseCase);

        }
        else
            throw new SyntaxException("Error");
        return conditionalE;
    }



    @Override
    public AST parse() throws PLCException,SyntaxException
    {
        lexInput = new String(inputParser);
        if(inputParser == "")
        {
            throw new SyntaxException("Empty Prog");
        }
        else
        {
            lexInput = inputParser.substring(currentPos,inputParser.length());
            scanner = CompilerComponentFactory.makeScanner(lexInput);
            firstToken = scanner.next();
            e = expr();

            //currentPos = Scanner.pos;

            return e;
        }

    }
}
