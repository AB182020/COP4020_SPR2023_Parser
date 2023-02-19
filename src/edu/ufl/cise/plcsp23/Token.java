package edu.ufl.cise.plcsp23;

public class Token implements IToken {
      final Kind kind;
      final int pos;
    final int length;
     final String value;
    SourceLocation sl;

    int line;
    int col;

    //constructor
    public Token(Kind kind, int pos, int length, String value, SourceLocation sl) {
        super();
        this.kind = kind;
        this.pos = pos;
        this.length = length;
        this.value =value;
        this.sl=sl;
    }


  //  @Override


    public SourceLocation getSourceLocation()
    {
        return  this.sl;
    }

    @Override
    public Kind getKind()
    {

        return this.kind;
    }
    public int getLength()
    {
        return this.length;
    }




    @Override
    public String getTokenString()
    {
        return this.value;
    }

//    private String toString(char[] source)
//    {
//        r
//    }
}
