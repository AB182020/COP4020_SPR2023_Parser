package edu.ufl.cise.plcsp23;

public class NumLitToken implements INumLitToken
{
    final Kind kind;
    final int pos;
    final int length;
    final int value;





    public NumLitToken(Kind kind, int pos, int length, int value) {
        super();
        this.kind = kind;
        this.pos = pos;
        this.length = length;
        this.value = value;

    }



    public int getValue()
    {
        return this.value;
    }



    @Override
    public SourceLocation getSourceLocation()
    {
        return null;
    }

    @Override
    public Kind getKind()
    {
        return Kind.NUM_LIT;
    }

    @Override
    public String getTokenString() {
        return null;
    }
}
