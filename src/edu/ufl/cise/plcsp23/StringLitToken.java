package edu.ufl.cise.plcsp23;

public class StringLitToken implements IStringLitToken
{
    final Kind kind;
    final int pos;
    final int length;
    String value;

    SourceLocation sl;
    public StringLitToken(Kind kind, int pos, int length, String value, SourceLocation sl)
    {
        this.kind = kind;
        this.pos = pos;
        this.length = length;
        this.value = value;
        this.sl =sl;
    }

    @Override
    public String getValue()
    {
        return this.value;
    }

    @Override
    public SourceLocation getSourceLocation()
    {

    return sl;
    }

    @Override
    public  Kind getKind()
    {
        return kind;
    }

    @Override
    public String getTokenString()
    {
        return value;
    }
}
