package edu.ufl.cise.plcsp23;

import edu.ufl.cise.plcsp23.ast.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Parser implements IParser
{

    AST astObj;
    String lexInput;

    String inputParser;
    final char[] inputParserChars;
    IToken.Kind kind;
    IToken nextToken;

    Expr rightE;
    Expr leftE;
    String lexTemp;
    Expr e = null;
    RandomExpr rnd;
    IdentExpr idnt;
    ZExpr z;
    NumLitExpr numLit;
    StringLitExpr stringLit;
    UnaryExpr unary;
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
    public Expr expr() throws SyntaxException, LexicalException {
      //  e = null;
        kind = firstToken.getKind();
       if(kind != IToken.Kind.RES_if)
       {
         leftE = orExpr();
       }
       else
         leftE =  conditionalExpr();
       return leftE;
    }
    public IToken consume() throws SyntaxException, LexicalException {
        currentPos++;
        lexTemp = inputParser.substring(currentPos,inputParser.length());
        scanner = CompilerComponentFactory.makeScanner(lexTemp);
        IToken token;
       token = scanner.next();
       kind = token.getKind();
//       currentPos = currentPos+ token.getTokenString().length();
       return token;

    }
    public Expr primaryExpr() throws SyntaxException, LexicalException {
        IToken  currentToken;
            if(nextToken == null)
            {
                currentToken = firstToken;
            }
            else
            {
                currentToken = nextToken;
            }

             nextToken = consume();
            if(currentToken.getKind() == IToken.Kind.NUM_LIT)
            {
                numLit = new NumLitExpr(currentToken);
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
            else if(currentToken.getKind() == IToken.Kind.RES_Z)
            {
                z = new ZExpr(currentToken);
                return z;
            }
            else if(currentToken.getKind() == IToken.Kind.IDENT)
            {
                idnt = new IdentExpr(currentToken);
                return idnt;
            }


        else if((kind == IToken.Kind.LPAREN))
        {
            consume();
            expr();
            if(firstToken.getKind() == IToken.Kind.RPAREN)
            {
                consume();
                //e = expr();

            }
            return e;

        }


        return e;
    }

    public Expr unaryExpr() throws SyntaxException, LexicalException {

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


    public Expr multiplicativeExpr() throws SyntaxException, LexicalException {
        leftE = null;
        rightE = null;
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
    public Expr additiveExpr() throws SyntaxException, LexicalException {
        leftE = null;
        rightE = null;
        kind = firstToken.getKind();
        leftE = multiplicativeExpr();

        while(kind == IToken.Kind.PLUS ||  kind == IToken.Kind.MINUS)
        {
            consume();
            rightE = multiplicativeExpr();
            // leftE =
        }
        return leftE;
    }
    public Expr powerExpr() throws SyntaxException, LexicalException {
        leftE = null;
        rightE = null;
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
    public Expr comparisonExpr() throws SyntaxException, LexicalException {
        leftE = null;
        rightE = null;
        kind = firstToken.getKind();
        leftE = powerExpr();

        while(kind == IToken.Kind.GT || kind == IToken.Kind.LT || kind == IToken.Kind.LE|| kind == IToken.Kind.GE)
        {
            consume();
            rightE = powerExpr();
            // leftE =
        }
        return leftE;
    }
    public Expr andExpr() throws SyntaxException, LexicalException {
        leftE = null;
        rightE = null;
        kind = firstToken.getKind();
        leftE = comparisonExpr();

        while(kind == IToken.Kind.AND || kind == IToken.Kind.BITAND)
        {
            consume();
            rightE = comparisonExpr();
            // leftE =
        }
        return leftE;
    }

    public Expr orExpr() throws SyntaxException, LexicalException {
        leftE = null;
        rightE = null;
        kind = firstToken.getKind();
       leftE = andExpr();

        while(kind == IToken.Kind.OR || kind == IToken.Kind.BITOR)
        {
            consume();
           rightE = andExpr();
          // leftE =
        }
        return leftE;
    }
    public Expr conditionalExpr()
    {
        return null;
    }



    @Override
    public AST parse() throws PLCException,SyntaxException
    {
        lexInput = new String(inputParser);
       scanner = CompilerComponentFactory.makeScanner(lexInput);
       if(inputParser == "")
       {
           throw new SyntaxException("Empty Prog");
       }
       else
       {
               firstToken = scanner.next();
               e = expr();

           return e;
       }

    }
}
