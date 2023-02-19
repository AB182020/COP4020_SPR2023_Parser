package edu.ufl.cise.plcsp23;
import java.util.Arrays;
import java.util.HashMap;


public class Scanner implements IScanner
{
    final String input;
    final char[] inputChars;
    int pos; //position of ch
    char ch; //next char
    Token token;
    NumLitToken NumToken;
    StringLitToken StrToken;
    HashMap<String, char[]> reservedWords = new HashMap<>();
    String currentToken="";
    State state = State.START;
    int currentPos = pos;
    LexicalException e;
    int line=1;
    int startCol=1;
    boolean esc = false;

    public Scanner(String input)
    {
        this.input = input;
        inputChars = Arrays.copyOf(input.toCharArray(),input.length()+1);
        ch = inputChars[pos];
    }

    public void CreateMapsRes(HashMap<String,char[]> maps)
    {
        //Maps ReservedWords
        maps.put("image","image".toCharArray());
        maps.put("pixel","pixel".toCharArray());
        maps.put("int","int".toCharArray());
        maps.put("string ","string ".toCharArray());
        maps.put("nil"," nil".toCharArray());
        maps.put("load","load".toCharArray());
        maps.put("display","display ".toCharArray());
        maps.put("x","x".toCharArray());
        maps.put("y","y".toCharArray());
        maps.put("a","a".toCharArray());
        maps.put("X","X".toCharArray());
        maps.put("Y","Y".toCharArray());
        maps.put("Z","Z".toCharArray());
        maps.put("r","r".toCharArray());
        maps.put("x_cart","x_cart".toCharArray());
        maps.put("y_cart","y_cart".toCharArray());
        maps.put("a_polar "," a_polar ".toCharArray());
        maps.put("r_polar","r_polar".toCharArray());
        maps.put("rand","rand ".toCharArray());
        maps.put("sin","sin ".toCharArray());
        maps.put("cos","cos".toCharArray());
        maps.put("atan","atan".toCharArray());
        maps.put("if","if".toCharArray());
        maps.put("while","while".toCharArray());
    }
    public void CreateMapsOp(HashMap<String,char[]> ops)
    {
        ops.put(".",".".toCharArray());
        ops.put(",",",".toCharArray());
        ops.put("?","?".toCharArray());
        ops.put(":",":".toCharArray());
        ops.put("(","(".toCharArray());
        ops.put(")",")".toCharArray());
        ops.put("<",">".toCharArray());
        ops.put("[","]".toCharArray());
        ops.put("{","}".toCharArray());
        ops.put("=","=".toCharArray());
        ops.put("==","==".toCharArray());
        ops.put("<->","<->".toCharArray());
        ops.put("<=","<=".toCharArray());
        ops.put(">=",">=".toCharArray());
        ops.put("!","!".toCharArray());
        ops.put("&","&".toCharArray());
        ops.put("&&","&&".toCharArray());
        ops.put("|","|".toCharArray());
        ops.put("||","||".toCharArray());
        ops.put("+","+".toCharArray());
        ops.put("-","-".toCharArray());
        ops.put("*","*".toCharArray());
        ops.put("**","**".toCharArray());
        ops.put("/","/".toCharArray());
        ops.put("%","%".toCharArray());

    }

    // enum for DFA
    private enum State
    {
        START,
        HAVE_EQ,
        IN_IDENT,
        IN_STR_LIT,
        IN_NUM_LIT

    }
//    @Override
//   public IToken scanToken() throws LexicalException
//    {
//        HashMap<String, char[]> ops = new HashMap<>();
//        CreateMapsRes(reservedWords);
//        CreateMapsOp(ops);
//         token = (Token) next();
//         return token;
//
//        }






    @Override
    public IToken next() throws LexicalException
    {
        HashMap<String, char[]> ops = new HashMap<>();
        CreateMapsRes(reservedWords);
       CreateMapsOp(ops);
        while(true) //read each character from input string until token is returned
        {switch (state)
        {
            case START ->
            {
                // String idents="";
                currentToken="";

                // identifier
                if((this.ch >= 'A' && this.ch <= 'Z') || (this.ch >= 'a' && this.ch <= 'z') ||(this.ch == '_'))
                {

                    currentToken = String.valueOf(this.ch);
                    currentPos = pos;
                    pos++;
//                    if(esc != true)
//                        startCol=pos;
                    // next();
                    this.ch = inputChars[pos];
                    while((this.ch >= 'A' && this.ch <= 'Z') || (this.ch >= 'a' && this.ch <= 'z') ||(this.ch == '_') || (this.ch =='0') || (this.ch>='1' && this.ch<='9') ||this.ch=='"')
                    {
                        currentToken = currentToken+this.ch;
                        currentPos =pos;
                        pos++;
                        if(!CheckEOF(pos))
                            this.ch = inputChars[pos];
                        else
                        {
                            if(CheckReserved(currentToken))
                            {
                                token = new Token(checkReservedKind(currentToken),pos,currentToken.length(),currentToken,new Token.SourceLocation(line,startCol));
                                return  token;
                            }
                            else
                            {
                                token = new Token(IToken.Kind.STRING_LIT,pos,currentToken.length(),currentToken,new Token.SourceLocation(line,startCol));
                                return  token;
                            }

                        }
                    }
                    state = State.START;
                    if(CheckReserved(currentToken))
                    {
                       // IToken.SourceLocation Src = new IToken.SourceLocation(line,startCol);
                         IToken token = new Token(checkReservedKind(currentToken),pos,currentToken.length(),currentToken,new Token.SourceLocation(line,startCol));
                         startCol+= currentToken.length();

//
                        return token;
                    }

                    else
                    {
                      IToken  token = new Token(IToken.Kind.IDENT,pos,currentToken.length(),currentToken,new Token.SourceLocation(line,startCol));
                      //
                        startCol+=currentToken.length();
                        return token;
                    }

                }
                //string literal
                if(this.ch =='"')
                {

                    currentToken = String.valueOf(this.ch);
                    currentPos = pos;
                    pos++;
//                    if(esc != true)
//                        startCol=pos;
                    // next();
                    this.ch = inputChars[pos];
                    while(this.ch != '"' && this.ch !='\n' &&  this.ch !='\t'&& this.ch !='\r'&& this.ch !='\f'&&this.ch !='\"'&&this.ch!='\\' )
                    {
                        currentToken = currentToken+this.ch;
                        currentPos =pos;
                        pos++;
                        if(!CheckEOF(pos))
                        {
                            this.ch = inputChars[pos];
                           // currentToken = currentToken+this.ch;
                        }
                    }
                    currentToken = currentToken+this.ch;
                    state = State.START;
                        IToken  StrToken = new StringLitToken(IToken.Kind.STRING_LIT,pos,currentToken.length(),currentToken,new Token.SourceLocation(line,startCol));
                        //
                        startCol+=currentToken.length();
                        return StrToken;


                }
                //digit
                else if (this.ch =='0' || (this.ch>='1' && this.ch<='9')  )
                {
                    currentToken = String.valueOf(this.ch);
//                    if(esc != true)
//                        startCol=pos;
                    if(this.ch!='0')
                    {
                        currentPos =pos;
                        pos++;
                        // next();
                        this.ch = inputChars[pos];
                        while(this.ch>='0' && this.ch<='9')
                        {
                            currentToken += this.ch;
                            currentPos =pos;
                            pos++;
                            if(!CheckEOF(pos))
                                this.ch = inputChars[pos];
                            else
                            {
                                try
                                {
                                    int Numint = Integer.parseInt(currentToken);
                                }
                                catch(NumberFormatException e)
                                {
                                    throw new LexicalException("Token too Big");
                                }
                              NumToken = new NumLitToken(IToken.Kind.NUM_LIT,pos,currentToken.length(),Integer.parseInt(currentToken));
                                startCol+=currentToken.length();
                                return  NumToken;

                            }
                        }
                    }
                    else
                    {
                        currentPos =pos;
                        pos++;
                        this.ch = inputChars[pos];
                    }
                    state = State.START;
                    try
                    {
                        int Numint = Integer.parseInt(currentToken);
                    }
                    catch(NumberFormatException e)
                    {
                        throw new LexicalException("Token too Big");
                    }
                    NumToken = new NumLitToken(IToken.Kind.NUM_LIT,pos,currentToken.length(),Integer.parseInt(currentToken));
                    startCol+=currentToken.length();
                    return NumToken;

                }
                //check for operator
                else if(this.ch =='.' || this.ch ==',' || this.ch=='?'|| this.ch==':'|| this.ch=='('|| this.ch==')'|| this.ch=='<'||this.ch=='>'|| this.ch=='['|| this.ch==']'|| this.ch=='{' || this.ch=='}' || this.ch=='='|| this.ch=='!' || this.ch=='&' || this.ch=='|'|| this.ch=='+'|| this.ch=='-'||this.ch=='*'||this.ch=='/'||this.ch=='%')
                {
                    String currentToken="";
//                    if(esc != true)
//                        startCol=pos;
                    currentToken = String.valueOf(this.ch);
                    //check for assign or equal
                    if(this.ch == '=')
                    {
                        currentPos = pos;
                        pos++;
                        if(!CheckEOF(pos))
                        {
                            this.ch = inputChars[pos];
                            if (this.ch == '=')
                            {
                                currentToken += this.ch;
                                currentPos = pos;
                                pos++;
                                this.ch = inputChars[pos];
                            }
                        }

                    }
                    // check for less than
                    else if(this.ch == '<')
                    {
                        currentPos = pos;
                        pos++;
                        if(!CheckEOF(pos))
                        {
                            this.ch = inputChars[pos];
                            if(this.ch =='=')
                            {
                                currentToken+=this.ch;
                                currentPos = pos;
                                pos++;
                                this.ch = inputChars[pos];
                            }
                            else if(this.ch =='-' && inputChars[pos+1] == '>')
                            {
                                currentToken+=this.ch;
                                currentPos = pos;
                                pos++;
                                this.ch = inputChars[pos];
                                currentToken+=this.ch;
                                currentPos = pos;
                                pos++;
                            }
                            else if(this.ch=='-' && inputChars[pos+1] != '>')
                            {
                                throw new LexicalException("Incomplete Exchange");
                            }
                        }

                    }

                    else if (this.ch =='>')
                    {
                        currentPos = pos;
                        pos++;
                        if(!CheckEOF(pos))
                        {
                            this.ch = inputChars[pos];
                            if(this.ch == '=')
                            {
                                currentToken+=this.ch;
                                currentPos = pos;
                                pos++;
                                this.ch = inputChars[pos];
                            }

                        }

                    }
                    else if(this.ch=='*')
                    {
                        currentPos = pos;
                        pos++;
                        if(!CheckEOF(pos))
                        {
                            this.ch = inputChars[pos];
                            if(this.ch == '*')
                            {
                                currentToken+=this.ch;
                                currentPos = pos;
                                pos++;
                                this.ch = inputChars[pos];
                            }

                        }
                    }
                    else if(this.ch =='&')
                    {
                        currentPos = pos;
                        pos++;
                        if(!CheckEOF(pos))
                        {
                            this.ch = inputChars[pos];
                            if(this.ch == '&')
                            {
                                currentToken+=this.ch;
                                currentPos = pos;
                                pos++;
                                this.ch = inputChars[pos];
                            }
                        }
                    }
                    else if(this.ch =='|')
                    {currentPos = pos;
                        pos++;
                        if(!CheckEOF(pos))
                        {
                            this.ch = inputChars[pos];
                            if(this.ch == '|')
                            {
                                currentToken+=this.ch;
                                currentPos = pos;
                                pos++;
                                this.ch = inputChars[pos];
                            }
                        }
                    }
                    else {
                        currentPos = pos;
                        pos++;
                        this.ch =inputChars[pos];
                    }
                    state = State.START;
                    token = new Token(checkOperator(currentToken),pos,currentToken.length(),currentToken,new Token.SourceLocation(line,startCol));
                    new IToken.SourceLocation(line,startCol);
                    startCol+=currentToken.length();
                    return token;
                }
                else if(this.ch =='\n' || this.ch == ' '||this.ch =='\t'||this.ch =='\r'||this.ch =='\f'||this.ch =='\"'||this.ch=='\\')
                {
                    esc = true;
                    if(this.ch =='\n')
                    {

                        line++;
                        startCol =1;
                    }
                    else
                    {
                        startCol++;
                    }
                        pos++;
                        currentToken += this.ch;
                        if (CheckEOF(pos)) {
                           IToken token = new Token(IToken.Kind.EOF, pos, currentToken.length(), currentToken,new Token.SourceLocation(line,startCol));
                            new IToken.SourceLocation(line,startCol);
                            startCol++;
                            return token;
                        } else
                        {
                            currentPos = pos;
                            this.ch = inputChars[pos];
                        }
                }

                else if(this.ch=='\0')
                {
                   //startCol = pos;
                   IToken token = new Token(IToken.Kind.EOF, pos, currentToken.length(), currentToken,new Token.SourceLocation(line,startCol));
                    new IToken.SourceLocation(line,startCol);
               //     startCol++;
                    return  token;
                }
                else if(this.ch =='~')
                {
                    currentToken = String.valueOf(this.ch);
                    currentPos = pos;
                    pos++;
                    this.ch = inputChars[pos];
                    while((this.ch >= 'A' && this.ch <= 'Z') || (this.ch >= 'a' && this.ch <= 'z') ||(this.ch == '_') || (this.ch =='0') || (this.ch>='1' && this.ch<='9') || this.ch ==' ')
                    {
                        currentToken = currentToken+this.ch;
                        currentPos =pos;
                        pos++;
                        if(!CheckEOF(pos))
                            this.ch = inputChars[pos];

                    }
                }
                else
                {
                    throw new LexicalException("Illegal Char");
                }





            }

        }

        }
    }

    public IToken.Kind checkOperator(String operator)
    {
        switch(operator)
        {
            case "==":
                return IToken.Kind.EQ;
            case "=":
                return IToken.Kind.ASSIGN;
            case "?":
                return IToken.Kind.QUESTION;
            case "<=":
                return IToken.Kind.LE;
            case "<":
                return IToken.Kind.LT;
            case ">":
                return IToken.Kind.GT;
            case "[":
                return IToken.Kind.LSQUARE;
            case "]":
                return IToken.Kind.RSQUARE;
            case"{":
                return IToken.Kind.LCURLY;
            case"}":
                return IToken.Kind.RCURLY;
            case")":
                return IToken.Kind.RPAREN;
            case"(":
                return IToken.Kind.LPAREN;
            case "!":
                return IToken.Kind.BANG;
            case "&":
                return IToken.Kind.BITAND;
            case"&&":
                return IToken.Kind.AND;
            case "|":
                return IToken.Kind.BITOR;
            case "||":
                return IToken.Kind.OR;
            case "+":
                return IToken.Kind.PLUS;
            case "-":
                return IToken.Kind.MINUS;
            case "*":
                return IToken.Kind.TIMES;
            case "/":
                return IToken.Kind.DIV;
            case "%":
                return IToken.Kind.MOD;
            case ">=":
                return IToken.Kind.GE;
            case"<->":
                return IToken.Kind.EXCHANGE;
            case "**":
                return IToken.Kind.EXP;
            default:
                return null;


        }

    }

    public boolean CheckEOF(int pos)
    {
        if(pos >= inputChars.length)
            return true;
        else
            return false;

    }

    public void printVals(HashMap<String, IToken.Kind> LexTokens)
    {
        LexTokens.forEach(((s, kind) -> System.out.println(s + ":"+ kind)));
    }
    public boolean CheckReserved(String str)
    {
        if(reservedWords.containsKey(str))
            return true;
        else
            return false;
    }
    public IToken.Kind checkReservedKind(String Keywrd)
    {
        switch(Keywrd)
        {
            case "a":
                return IToken.Kind.RES_a;
            case "int":
                return IToken.Kind.RES_int;
            case "cos":
                return IToken.Kind.RES_cos;
            case "image":
                return IToken.Kind.RES_image;
            case "void":
                return IToken.Kind.RES_void;
            case"nil":
                return IToken.Kind.RES_nil;
            case"load":
                return IToken.Kind.RES_load;
            case "display":
                return IToken.Kind.RES_display;
            case "write":
                return IToken.Kind.RES_write;
            case "x":
                return IToken.Kind.RES_x;
            case "X":
                return IToken.Kind.RES_X;
            case "x_cart":
                return IToken.Kind.RES_x_cart;
            case "y":
                return IToken.Kind.RES_y;
            case"Y":
                return IToken.Kind.RES_Y;
            case "y_cart":
                return IToken.Kind.RES_y_cart;
            case "Z":
                return IToken.Kind.RES_Z;
            case "a_polar":
                return IToken.Kind.RES_a_polar;
            case"r_polar":
                return IToken.Kind.RES_r_polar;
            case"atan":
                return IToken.Kind.RES_atan;
            case"sin":
                return IToken.Kind.RES_sin;
            case"if":
                return IToken.Kind.RES_if;
            case "while":
                return IToken.Kind.RES_while;
            case "rand":
                return IToken.Kind.RES_rand;
            default:
                return null;


        }

    }
}
