package edu.ufl.cise.plcsp23;

import edu.ufl.cise.plcsp23.ast.*;


public class exprEvalVisitor implements ASTVisitor,IToken
{
    @Override
    public Object visitConditionalExpr(ConditionalExpr conditionalExpr, Object arg) throws PLCException {
        return null;
    }




    @Override
    public Object visitBinaryExpr(BinaryExpr binaryExpr, Object arg) throws PLCException {
        int left = (Integer) binaryExpr.getLeft().visit(this,arg);
        int right = (Integer ) binaryExpr.getLeft().visit(this,arg);
        Kind opKind = binaryExpr.getOp();
        int val = switch(opKind) {
            case PLUS -> left + right;
            case MINUS -> left - right;
            case TIMES -> left * right;
            case DIV -> left/right;
            default -> {throw new SyntaxException("Error");}
        };
        return val;
    }

    @Override
    public Object visitUnaryExpr(UnaryExpr unaryExpr, Object arg) throws PLCException {
        return null;
    }

    @Override
    public Object visitStringLitExpr(StringLitExpr stringLitExpr, Object arg) throws PLCException
    {
        return stringLitExpr.getValue();
    }

    @Override
    public Object visitNumLitExpr(NumLitExpr numLitExpr, Object arg) throws PLCException {
        return numLitExpr.getValue();
    }

    @Override
    public Object visitIdentExpr(IdentExpr identExpr, Object arg) throws PLCException {
        return identExpr.getFirstToken();
    }

    @Override
    public Object visitZExpr(ZExpr constExpr, Object arg) throws PLCException {
        return constExpr.getValue();
    }

    @Override
    public Object visitRandomExpr(RandomExpr randomExpr, Object arg) throws PLCException {
        return null;
    }

    @Override
    public SourceLocation getSourceLocation() {
        return null;
    }

    @Override
    public Kind getKind() {
        return null;
    }

    @Override
    public String getTokenString() {
        return null;
    }
}
