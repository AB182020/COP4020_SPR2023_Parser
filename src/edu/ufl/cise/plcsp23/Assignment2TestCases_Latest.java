package edu.ufl.cise.plcsp23;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import org.junit.jupiter.api.Test;

import edu.ufl.cise.plcsp23.IToken.Kind;
import edu.ufl.cise.plcsp23.ast.AST;
import edu.ufl.cise.plcsp23.ast.BinaryExpr;
import edu.ufl.cise.plcsp23.ast.ConditionalExpr;
import edu.ufl.cise.plcsp23.ast.Expr;
import edu.ufl.cise.plcsp23.ast.IdentExpr;
import edu.ufl.cise.plcsp23.ast.NumLitExpr;
import edu.ufl.cise.plcsp23.ast.RandomExpr;
import edu.ufl.cise.plcsp23.ast.StringLitExpr;
import edu.ufl.cise.plcsp23.ast.UnaryExpr;
import edu.ufl.cise.plcsp23.ast.ZExpr;
class Assignment2TestGrading{
    static final int TIMEOUT_MILLIS = 1000;
    /** Constructs a scanner and parser for the given input string, scans and parses the input and returns and AST.
     *
     * @param input   String representing program to be tested
     * @return  AST representing the program
     * @throws PLCException
     */
    AST getAST(String input) throws  PLCException {
        return  CompilerComponentFactory.makeAssignment2Parser(input).parse();
    }

    /**
     * Checks that the given AST e has type NumLitExpr with the indicated value.  Returns the given AST cast to NumLitExpr.
     *
     * @param e
     * @param value
     * @return
     */
    NumLitExpr checkNumLit(AST e, int value) {
        assertThat("",e, instanceOf( NumLitExpr.class));
        NumLitExpr ne = (NumLitExpr)e;
        assertEquals(value, ne.getValue());
        return ne;
    }

    /**
     *  Checks that the given AST e has type StringLitExpr with the given String value.  Returns the given AST cast to StringLitExpr.
     * @param e
    // * @param name
     * @return
     */
    StringLitExpr checkStringLit(AST e, String value) {
        assertThat("",e, instanceOf( StringLitExpr.class));
        StringLitExpr se = (StringLitExpr)e;
        assertEquals(value,se.getValue());
        return se;
    }

    /**
     *  Checks that the given AST e has type UnaryExpr with the given operator.  Returns the given AST cast to UnaryExpr.
     * @param e
     * @param op  Kind of expected operator
     * @return
     */
    private UnaryExpr checkUnary(AST e, Kind op) {
        assertThat("",e, instanceOf( UnaryExpr.class));
        assertEquals(op, ((UnaryExpr)e).getOp());
        return (UnaryExpr)e;
    }


    /**
     *  Checks that the given AST e has type ConditionalExpr.  Returns the given AST cast to ConditionalExpr.
     * @param e
     * @return
     */
    private ConditionalExpr checkConditional(AST e) {
        assertThat("",e, instanceOf( ConditionalExpr.class));
        return (ConditionalExpr)e;
    }

    /**
     *  Checks that the given AST e has type BinaryExpr with the given operator.  Returns the given AST cast to BinaryExpr.
     *
     * @param e
    // * @param op  Kind of expected operator
     * @return
     */
    BinaryExpr checkBinary(AST e, Kind expectedOp) {
        assertThat("",e, instanceOf(BinaryExpr.class));
        BinaryExpr be = (BinaryExpr)e;
        assertEquals(expectedOp, be.getOp());
        return be;
    }

    /**
     * Checks that the given AST e has type IdentExpr with the given name.  Returns the given AST cast to IdentExpr.
     * @param e
     * @param name
     * @return
     */
    IdentExpr checkIdent(AST e, String name) {
        assertThat("",e, instanceOf( IdentExpr.class));
        IdentExpr ident = (IdentExpr)e;
        assertEquals(name,ident.getName());
        return ident;
    }
    @Test
    void test0() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
3
""";
            AST ast = getAST(input);
            checkNumLit(ast,3);});
    }

    @Test
    void test1() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
b
""";
            AST ast = getAST(input);
            checkIdent(ast,"b");});
    }

    @Test
    void test2() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
"hello"
""";
            AST ast = getAST(input);
            checkStringLit(ast,"hello");});
    }

    @Test
    void test3() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 Z  """;
            AST ast = getAST(input);
            assertThat("",ast,instanceOf(ZExpr.class));});
    }

    @Test
    void test4() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 rand 
""";
            AST ast = getAST(input);
            assertThat("",ast,instanceOf(RandomExpr.class));});
    }

    @Test
    void test5() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 -4""";
            AST ast = getAST(input);

            checkUnary(ast,Kind.MINUS);
            Expr v0 = ((UnaryExpr)ast).getE();
            checkNumLit(v0,4);});
    }

    @Test
    void test6() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
b-b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.MINUS);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"b");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test7() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
12 | b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.BITOR);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkNumLit(v0,12);Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test8() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
z || b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.OR);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"z");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test9() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 Z & b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.BITAND);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            assertThat("",v0,instanceOf(ZExpr.class));Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test10() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q && b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.AND);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"q");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test11() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q < b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.LT);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"q");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test12() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q > b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.GT);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"q");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test13() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q == b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.EQ);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"q");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test14() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q <= b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.LE);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"q");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test15() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q >= b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.GE);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"q");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test16() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q ** b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.EXP);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"q");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test17() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q * b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.TIMES);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"q");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test18() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q / b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.DIV);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"q");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test19() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q % b""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.MOD);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"q");Expr v1 = ((BinaryExpr)ast).getRight();
            checkIdent(v1,"b");});
    }

    @Test
    void test20() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
!q""";
            AST ast = getAST(input);

            checkUnary(ast,Kind.BANG);
            Expr v0 = ((UnaryExpr)ast).getE();
            checkIdent(v0,"q");});
    }

    @Test
    void test21() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
!!!(!!q)""";
            AST ast = getAST(input);

            checkUnary(ast,Kind.BANG);
            Expr v0 = ((UnaryExpr)ast).getE();

            checkUnary(v0,Kind.BANG);
            Expr v1 = ((UnaryExpr)v0).getE();

            checkUnary(v1,Kind.BANG);
            Expr v2 = ((UnaryExpr)v1).getE();

            checkUnary(v2,Kind.BANG);
            Expr v3 = ((UnaryExpr)v2).getE();

            checkUnary(v3,Kind.BANG);
            Expr v4 = ((UnaryExpr)v3).getE();
            checkIdent(v4,"q");});
    }

    @Test
    void test22() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
atan ata""";
            AST ast = getAST(input);

            checkUnary(ast,Kind.RES_atan);
            Expr v0 = ((UnaryExpr)ast).getE();
            checkIdent(v0,"ata");});
    }

    @Test
    void test23() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 if d ? e ? f""";
            AST ast = getAST(input);
            checkConditional(ast);
            Expr v0 = ((ConditionalExpr)ast).getGuard();
            checkIdent(v0,"d");Expr v1 = ((ConditionalExpr)ast).getTrueCase();
            checkIdent(v1,"e");Expr v2 = ((ConditionalExpr)ast).getFalseCase();
            checkIdent(v2,"f");});
    }

    @Test
    void test24() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
"Go Gators" """;
            AST ast = getAST(input);
            checkStringLit(ast,"Go Gators");});
    }

    @Test
    void test25() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 (3) """;
            AST ast = getAST(input);
            checkNumLit(ast,3);});
    }

    @Test
    void test26() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 (!3) """;
            AST ast = getAST(input);

            checkUnary(ast,Kind.BANG);
            Expr v0 = ((UnaryExpr)ast).getE();
            checkNumLit(v0,3);});
    }

    @Test
    void test27() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 -3 """;
            AST ast = getAST(input);

            checkUnary(ast,Kind.MINUS);
            Expr v0 = ((UnaryExpr)ast).getE();
            checkNumLit(v0,3);});
    }

    @Test
    void test28() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
---3""";
            AST ast = getAST(input);

            checkUnary(ast,Kind.MINUS);
            Expr v0 = ((UnaryExpr)ast).getE();

            checkUnary(v0,Kind.MINUS);
            Expr v1 = ((UnaryExpr)v0).getE();

            checkUnary(v1,Kind.MINUS);
            Expr v2 = ((UnaryExpr)v1).getE();
            checkNumLit(v2,3);});
    }

    @Test
    void test29() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 (-3) """;
            AST ast = getAST(input);

            checkUnary(ast,Kind.MINUS);
            Expr v0 = ((UnaryExpr)ast).getE();
            checkNumLit(v0,3);});
    }

    @Test
    void test30() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 sin 56 """;
            AST ast = getAST(input);

            checkUnary(ast,Kind.RES_sin);
            Expr v0 = ((UnaryExpr)ast).getE();
            checkNumLit(v0,56);});
    }

    @Test
    void test31() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 cos - 56 """;
            AST ast = getAST(input);

            checkUnary(ast,Kind.RES_cos);
            Expr v0 = ((UnaryExpr)ast).getE();

            checkUnary(v0,Kind.MINUS);
            Expr v1 = ((UnaryExpr)v0).getE();
            checkNumLit(v1,56);});
    }

    @Test
    void test32() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 cos atan ! - "hello" """;
            AST ast = getAST(input);

            checkUnary(ast,Kind.RES_cos);
            Expr v0 = ((UnaryExpr)ast).getE();

            checkUnary(v0,Kind.RES_atan);
            Expr v1 = ((UnaryExpr)v0).getE();

            checkUnary(v1,Kind.BANG);
            Expr v2 = ((UnaryExpr)v1).getE();

            checkUnary(v2,Kind.MINUS);
            Expr v3 = ((UnaryExpr)v2).getE();
            checkStringLit(v3,"hello");});
    }

    @Test
    void test33() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
1-2+3*4/5%6""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.PLUS);
            Expr v0 = ((BinaryExpr)ast).getLeft();

            checkBinary(v0,Kind.MINUS);
            Expr v1 = ((BinaryExpr)v0).getLeft();
            checkNumLit(v1,1);Expr v2 = ((BinaryExpr)v0).getRight();
            checkNumLit(v2,2);Expr v3 = ((BinaryExpr)ast).getRight();

            checkBinary(v3,Kind.MOD);
            Expr v4 = ((BinaryExpr)v3).getLeft();

            checkBinary(v4,Kind.DIV);
            Expr v5 = ((BinaryExpr)v4).getLeft();

            checkBinary(v5,Kind.TIMES);
            Expr v6 = ((BinaryExpr)v5).getLeft();
            checkNumLit(v6,3);Expr v7 = ((BinaryExpr)v5).getRight();
            checkNumLit(v7,4);Expr v8 = ((BinaryExpr)v4).getRight();
            checkNumLit(v8,5);Expr v9 = ((BinaryExpr)v3).getRight();
            checkNumLit(v9,6);});
    }

    @Test
    void test34() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
!-!-sin-atan 4""";
            AST ast = getAST(input);

            checkUnary(ast,Kind.BANG);
            Expr v0 = ((UnaryExpr)ast).getE();

            checkUnary(v0,Kind.MINUS);
            Expr v1 = ((UnaryExpr)v0).getE();

            checkUnary(v1,Kind.BANG);
            Expr v2 = ((UnaryExpr)v1).getE();

            checkUnary(v2,Kind.MINUS);
            Expr v3 = ((UnaryExpr)v2).getE();

            checkUnary(v3,Kind.RES_sin);
            Expr v4 = ((UnaryExpr)v3).getE();

            checkUnary(v4,Kind.MINUS);
            Expr v5 = ((UnaryExpr)v4).getE();

            checkUnary(v5,Kind.RES_atan);
            Expr v6 = ((UnaryExpr)v5).getE();
            checkNumLit(v6,4);});
    }

    @Test
    void test35() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
if 2+5>0 ? "hello" ? "goodbye" + 3
""";
            AST ast = getAST(input);
            checkConditional(ast);
            Expr v0 = ((ConditionalExpr)ast).getGuard();

            checkBinary(v0,Kind.GT);
            Expr v1 = ((BinaryExpr)v0).getLeft();

            checkBinary(v1,Kind.PLUS);
            Expr v2 = ((BinaryExpr)v1).getLeft();
            checkNumLit(v2,2);Expr v3 = ((BinaryExpr)v1).getRight();
            checkNumLit(v3,5);Expr v4 = ((BinaryExpr)v0).getRight();
            checkNumLit(v4,0);Expr v5 = ((ConditionalExpr)ast).getTrueCase();
            checkStringLit(v5,"hello");Expr v6 = ((ConditionalExpr)ast).getFalseCase();

            checkBinary(v6,Kind.PLUS);
            Expr v7 = ((BinaryExpr)v6).getLeft();
            checkStringLit(v7,"goodbye");Expr v8 = ((BinaryExpr)v6).getRight();
            checkNumLit(v8,3);});
    }

    @Test
    void test36() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
if if 3 ? 4 ? 5 ? if 6 ? 7 ? 8 ? if 9 ? 10 ? 11
""";
            AST ast = getAST(input);
            checkConditional(ast);
            Expr v0 = ((ConditionalExpr)ast).getGuard();
            checkConditional(v0);
            Expr v1 = ((ConditionalExpr)v0).getGuard();
            checkNumLit(v1,3);Expr v2 = ((ConditionalExpr)v0).getTrueCase();
            checkNumLit(v2,4);Expr v3 = ((ConditionalExpr)v0).getFalseCase();
            checkNumLit(v3,5);Expr v4 = ((ConditionalExpr)ast).getTrueCase();
            checkConditional(v4);
            Expr v5 = ((ConditionalExpr)v4).getGuard();
            checkNumLit(v5,6);Expr v6 = ((ConditionalExpr)v4).getTrueCase();
            checkNumLit(v6,7);Expr v7 = ((ConditionalExpr)v4).getFalseCase();
            checkNumLit(v7,8);Expr v8 = ((ConditionalExpr)ast).getFalseCase();
            checkConditional(v8);
            Expr v9 = ((ConditionalExpr)v8).getGuard();
            checkNumLit(v9,9);Expr v10 = ((ConditionalExpr)v8).getTrueCase();
            checkNumLit(v10,10);Expr v11 = ((ConditionalExpr)v8).getFalseCase();
            checkNumLit(v11,11);});
    }

    @Test
    void test37() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
b+2""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.PLUS);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkIdent(v0,"b");Expr v1 = ((BinaryExpr)ast).getRight();
            checkNumLit(v1,2);});
    }

    @Test
    void test38() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
3 + (if d ? b ? c)""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.PLUS);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkNumLit(v0,3);Expr v1 = ((BinaryExpr)ast).getRight();
            checkConditional(v1);
            Expr v2 = ((ConditionalExpr)v1).getGuard();
            checkIdent(v2,"d");Expr v3 = ((ConditionalExpr)v1).getTrueCase();
            checkIdent(v3,"b");Expr v4 = ((ConditionalExpr)v1).getFalseCase();
            checkIdent(v4,"c");});
    }

    @Test
    void test39() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
if d ? b ? c + 3""";
            AST ast = getAST(input);
            checkConditional(ast);
            Expr v0 = ((ConditionalExpr)ast).getGuard();
            checkIdent(v0,"d");Expr v1 = ((ConditionalExpr)ast).getTrueCase();
            checkIdent(v1,"b");Expr v2 = ((ConditionalExpr)ast).getFalseCase();

            checkBinary(v2,Kind.PLUS);
            Expr v3 = ((BinaryExpr)v2).getLeft();
            checkIdent(v3,"c");Expr v4 = ((BinaryExpr)v2).getRight();
            checkNumLit(v4,3);});
    }

    @Test
    void test40() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
if
d+ (if g ? h? k)
?
if gg ? hh ? kk + 5
?
"hello"
""";
            AST ast = getAST(input);
            checkConditional(ast);
            Expr v0 = ((ConditionalExpr)ast).getGuard();

            checkBinary(v0,Kind.PLUS);
            Expr v1 = ((BinaryExpr)v0).getLeft();
            checkIdent(v1,"d");Expr v2 = ((BinaryExpr)v0).getRight();
            checkConditional(v2);
            Expr v3 = ((ConditionalExpr)v2).getGuard();
            checkIdent(v3,"g");Expr v4 = ((ConditionalExpr)v2).getTrueCase();
            checkIdent(v4,"h");Expr v5 = ((ConditionalExpr)v2).getFalseCase();
            checkIdent(v5,"k");Expr v6 = ((ConditionalExpr)ast).getTrueCase();
            checkConditional(v6);
            Expr v7 = ((ConditionalExpr)v6).getGuard();
            checkIdent(v7,"gg");Expr v8 = ((ConditionalExpr)v6).getTrueCase();
            checkIdent(v8,"hh");Expr v9 = ((ConditionalExpr)v6).getFalseCase();

            checkBinary(v9,Kind.PLUS);
            Expr v10 = ((BinaryExpr)v9).getLeft();
            checkIdent(v10,"kk");Expr v11 = ((BinaryExpr)v9).getRight();
            checkNumLit(v11,5);Expr v12 = ((ConditionalExpr)ast).getFalseCase();
            checkStringLit(v12,"hello");});
    }

    @Test
    void test41() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
if (if (3) ? (1) ? (2)) ? (if (6) ? (3) ? (9)) ? (if (0) ? (-3) ? (2**5))""";
            AST ast = getAST(input);
            checkConditional(ast);
            Expr v0 = ((ConditionalExpr)ast).getGuard();
            checkConditional(v0);
            Expr v1 = ((ConditionalExpr)v0).getGuard();
            checkNumLit(v1,3);Expr v2 = ((ConditionalExpr)v0).getTrueCase();
            checkNumLit(v2,1);Expr v3 = ((ConditionalExpr)v0).getFalseCase();
            checkNumLit(v3,2);Expr v4 = ((ConditionalExpr)ast).getTrueCase();
            checkConditional(v4);
            Expr v5 = ((ConditionalExpr)v4).getGuard();
            checkNumLit(v5,6);Expr v6 = ((ConditionalExpr)v4).getTrueCase();
            checkNumLit(v6,3);Expr v7 = ((ConditionalExpr)v4).getFalseCase();
            checkNumLit(v7,9);Expr v8 = ((ConditionalExpr)ast).getFalseCase();
            checkConditional(v8);
            Expr v9 = ((ConditionalExpr)v8).getGuard();
            checkNumLit(v9,0);Expr v10 = ((ConditionalExpr)v8).getTrueCase();

            checkUnary(v10,Kind.MINUS);
            Expr v11 = ((UnaryExpr)v10).getE();
            checkNumLit(v11,3);Expr v12 = ((ConditionalExpr)v8).getFalseCase();

            checkBinary(v12,Kind.EXP);
            Expr v13 = ((BinaryExpr)v12).getLeft();
            checkNumLit(v13,2);Expr v14 = ((BinaryExpr)v12).getRight();
            checkNumLit(v14,5);});
    }

    @Test
    void test42() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
if if 3 ? 1 ? 2 ? if 6 ? 3 ? 9 ? if 0 ? -3 ? 2**5""";
            AST ast = getAST(input);
            checkConditional(ast);
            Expr v0 = ((ConditionalExpr)ast).getGuard();
            checkConditional(v0);
            Expr v1 = ((ConditionalExpr)v0).getGuard();
            checkNumLit(v1,3);Expr v2 = ((ConditionalExpr)v0).getTrueCase();
            checkNumLit(v2,1);Expr v3 = ((ConditionalExpr)v0).getFalseCase();
            checkNumLit(v3,2);Expr v4 = ((ConditionalExpr)ast).getTrueCase();
            checkConditional(v4);
            Expr v5 = ((ConditionalExpr)v4).getGuard();
            checkNumLit(v5,6);Expr v6 = ((ConditionalExpr)v4).getTrueCase();
            checkNumLit(v6,3);Expr v7 = ((ConditionalExpr)v4).getFalseCase();
            checkNumLit(v7,9);Expr v8 = ((ConditionalExpr)ast).getFalseCase();
            checkConditional(v8);
            Expr v9 = ((ConditionalExpr)v8).getGuard();
            checkNumLit(v9,0);Expr v10 = ((ConditionalExpr)v8).getTrueCase();

            checkUnary(v10,Kind.MINUS);
            Expr v11 = ((UnaryExpr)v10).getE();
            checkNumLit(v11,3);Expr v12 = ((ConditionalExpr)v8).getFalseCase();

            checkBinary(v12,Kind.EXP);
            Expr v13 = ((BinaryExpr)v12).getLeft();
            checkNumLit(v13,2);Expr v14 = ((BinaryExpr)v12).getRight();
            checkNumLit(v14,5);});
    }

    @Test
    void test43() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
if if 3 ? 1 ? 2 ? if 5 ** 6 ? 3 ? 9 ? if 0 ? -3 ? 2**5""";
            AST ast = getAST(input);
            checkConditional(ast);
            Expr v0 = ((ConditionalExpr)ast).getGuard();
            checkConditional(v0);
            Expr v1 = ((ConditionalExpr)v0).getGuard();
            checkNumLit(v1,3);Expr v2 = ((ConditionalExpr)v0).getTrueCase();
            checkNumLit(v2,1);Expr v3 = ((ConditionalExpr)v0).getFalseCase();
            checkNumLit(v3,2);Expr v4 = ((ConditionalExpr)ast).getTrueCase();
            checkConditional(v4);
            Expr v5 = ((ConditionalExpr)v4).getGuard();

            checkBinary(v5,Kind.EXP);
            Expr v6 = ((BinaryExpr)v5).getLeft();
            checkNumLit(v6,5);Expr v7 = ((BinaryExpr)v5).getRight();
            checkNumLit(v7,6);Expr v8 = ((ConditionalExpr)v4).getTrueCase();
            checkNumLit(v8,3);Expr v9 = ((ConditionalExpr)v4).getFalseCase();
            checkNumLit(v9,9);Expr v10 = ((ConditionalExpr)ast).getFalseCase();
            checkConditional(v10);
            Expr v11 = ((ConditionalExpr)v10).getGuard();
            checkNumLit(v11,0);Expr v12 = ((ConditionalExpr)v10).getTrueCase();

            checkUnary(v12,Kind.MINUS);
            Expr v13 = ((UnaryExpr)v12).getE();
            checkNumLit(v13,3);Expr v14 = ((ConditionalExpr)v10).getFalseCase();

            checkBinary(v14,Kind.EXP);
            Expr v15 = ((BinaryExpr)v14).getLeft();
            checkNumLit(v15,2);Expr v16 = ((BinaryExpr)v14).getRight();
            checkNumLit(v16,5);});
    }

    @Test
    void test44() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
! 5 || 5 | -32 + 5**2 & 6 ** -1 && "abc" % rand""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.BITOR);
            Expr v0 = ((BinaryExpr)ast).getLeft();

            checkBinary(v0,Kind.OR);
            Expr v1 = ((BinaryExpr)v0).getLeft();

            checkUnary(v1,Kind.BANG);
            Expr v2 = ((UnaryExpr)v1).getE();
            checkNumLit(v2,5);Expr v3 = ((BinaryExpr)v0).getRight();
            checkNumLit(v3,5);Expr v4 = ((BinaryExpr)ast).getRight();

            checkBinary(v4,Kind.AND);
            Expr v5 = ((BinaryExpr)v4).getLeft();

            checkBinary(v5,Kind.BITAND);
            Expr v6 = ((BinaryExpr)v5).getLeft();

            checkBinary(v6,Kind.EXP);
            Expr v7 = ((BinaryExpr)v6).getLeft();

            checkBinary(v7,Kind.PLUS);
            Expr v8 = ((BinaryExpr)v7).getLeft();

            checkUnary(v8,Kind.MINUS);
            Expr v9 = ((UnaryExpr)v8).getE();
            checkNumLit(v9,32);Expr v10 = ((BinaryExpr)v7).getRight();
            checkNumLit(v10,5);Expr v11 = ((BinaryExpr)v6).getRight();
            checkNumLit(v11,2);Expr v12 = ((BinaryExpr)v5).getRight();

            checkBinary(v12,Kind.EXP);
            Expr v13 = ((BinaryExpr)v12).getLeft();
            checkNumLit(v13,6);Expr v14 = ((BinaryExpr)v12).getRight();

            checkUnary(v14,Kind.MINUS);
            Expr v15 = ((UnaryExpr)v14).getE();
            checkNumLit(v15,1);Expr v16 = ((BinaryExpr)v4).getRight();

            checkBinary(v16,Kind.MOD);
            Expr v17 = ((BinaryExpr)v16).getLeft();
            checkStringLit(v17,"abc");Expr v18 = ((BinaryExpr)v16).getRight();
            assertThat("",v18,instanceOf(RandomExpr.class));});
    }

    @Test
    void test45() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
32 / "str" == ident + Z & rand - cos 52 <= 12 ** ! "qwe" >= _abc | 321 < "123" > abc ** 462 - 156 && 12 % 1 && atan 0 - 0 * 12 || 8 --sin 9""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.OR);
            Expr v0 = ((BinaryExpr)ast).getLeft();

            checkBinary(v0,Kind.BITOR);
            Expr v1 = ((BinaryExpr)v0).getLeft();

            checkBinary(v1,Kind.BITAND);
            Expr v2 = ((BinaryExpr)v1).getLeft();

            checkBinary(v2,Kind.EQ);
            Expr v3 = ((BinaryExpr)v2).getLeft();

            checkBinary(v3,Kind.DIV);
            Expr v4 = ((BinaryExpr)v3).getLeft();
            checkNumLit(v4,32);Expr v5 = ((BinaryExpr)v3).getRight();
            checkStringLit(v5,"str");Expr v6 = ((BinaryExpr)v2).getRight();

            checkBinary(v6,Kind.PLUS);
            Expr v7 = ((BinaryExpr)v6).getLeft();
            checkIdent(v7,"ident");Expr v8 = ((BinaryExpr)v6).getRight();
            assertThat("",v8,instanceOf(ZExpr.class));Expr v9 = ((BinaryExpr)v1).getRight();

            checkBinary(v9,Kind.GE);
            Expr v10 = ((BinaryExpr)v9).getLeft();

            checkBinary(v10,Kind.LE);
            Expr v11 = ((BinaryExpr)v10).getLeft();

            checkBinary(v11,Kind.MINUS);
            Expr v12 = ((BinaryExpr)v11).getLeft();
            assertThat("",v12,instanceOf(RandomExpr.class));Expr v13 = ((BinaryExpr)v11).getRight();

            checkUnary(v13,Kind.RES_cos);
            Expr v14 = ((UnaryExpr)v13).getE();
            checkNumLit(v14,52);Expr v15 = ((BinaryExpr)v10).getRight();

            checkBinary(v15,Kind.EXP);
            Expr v16 = ((BinaryExpr)v15).getLeft();
            checkNumLit(v16,12);Expr v17 = ((BinaryExpr)v15).getRight();

            checkUnary(v17,Kind.BANG);
            Expr v18 = ((UnaryExpr)v17).getE();
            checkStringLit(v18,"qwe");Expr v19 = ((BinaryExpr)v9).getRight();
            checkIdent(v19,"_abc");Expr v20 = ((BinaryExpr)v0).getRight();

            checkBinary(v20,Kind.AND);
            Expr v21 = ((BinaryExpr)v20).getLeft();

            checkBinary(v21,Kind.AND);
            Expr v22 = ((BinaryExpr)v21).getLeft();

            checkBinary(v22,Kind.GT);
            Expr v23 = ((BinaryExpr)v22).getLeft();

            checkBinary(v23,Kind.LT);
            Expr v24 = ((BinaryExpr)v23).getLeft();
            checkNumLit(v24,321);Expr v25 = ((BinaryExpr)v23).getRight();
            checkStringLit(v25,"123");Expr v26 = ((BinaryExpr)v22).getRight();

            checkBinary(v26,Kind.EXP);
            Expr v27 = ((BinaryExpr)v26).getLeft();
            checkIdent(v27,"abc");Expr v28 = ((BinaryExpr)v26).getRight();

            checkBinary(v28,Kind.MINUS);
            Expr v29 = ((BinaryExpr)v28).getLeft();
            checkNumLit(v29,462);Expr v30 = ((BinaryExpr)v28).getRight();
            checkNumLit(v30,156);Expr v31 = ((BinaryExpr)v21).getRight();

            checkBinary(v31,Kind.MOD);
            Expr v32 = ((BinaryExpr)v31).getLeft();
            checkNumLit(v32,12);Expr v33 = ((BinaryExpr)v31).getRight();
            checkNumLit(v33,1);Expr v34 = ((BinaryExpr)v20).getRight();

            checkBinary(v34,Kind.MINUS);
            Expr v35 = ((BinaryExpr)v34).getLeft();

            checkUnary(v35,Kind.RES_atan);
            Expr v36 = ((UnaryExpr)v35).getE();
            checkNumLit(v36,0);Expr v37 = ((BinaryExpr)v34).getRight();

            checkBinary(v37,Kind.TIMES);
            Expr v38 = ((BinaryExpr)v37).getLeft();
            checkNumLit(v38,0);Expr v39 = ((BinaryExpr)v37).getRight();
            checkNumLit(v39,12);Expr v40 = ((BinaryExpr)ast).getRight();

            checkBinary(v40,Kind.MINUS);
            Expr v41 = ((BinaryExpr)v40).getLeft();
            checkNumLit(v41,8);Expr v42 = ((BinaryExpr)v40).getRight();

            checkUnary(v42,Kind.MINUS);
            Expr v43 = ((UnaryExpr)v42).getE();

            checkUnary(v43,Kind.RES_sin);
            Expr v44 = ((UnaryExpr)v43).getE();
            checkNumLit(v44,9);});
    }

    @Test
    void test46() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
(32 / "str") == ident + Z & rand - cos (52 <= 12 ** ! ("qwe" >= ((_abc) | 321) < "123" > abc ** 462) - 156 && 12 % (1 && atan (0)) - 0 * 12 || (8 --sin 9))""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.BITAND);
            Expr v0 = ((BinaryExpr)ast).getLeft();

            checkBinary(v0,Kind.EQ);
            Expr v1 = ((BinaryExpr)v0).getLeft();

            checkBinary(v1,Kind.DIV);
            Expr v2 = ((BinaryExpr)v1).getLeft();
            checkNumLit(v2,32);Expr v3 = ((BinaryExpr)v1).getRight();
            checkStringLit(v3,"str");Expr v4 = ((BinaryExpr)v0).getRight();

            checkBinary(v4,Kind.PLUS);
            Expr v5 = ((BinaryExpr)v4).getLeft();
            checkIdent(v5,"ident");Expr v6 = ((BinaryExpr)v4).getRight();
            assertThat("",v6,instanceOf(ZExpr.class));Expr v7 = ((BinaryExpr)ast).getRight();

            checkBinary(v7,Kind.MINUS);
            Expr v8 = ((BinaryExpr)v7).getLeft();
            assertThat("",v8,instanceOf(RandomExpr.class));Expr v9 = ((BinaryExpr)v7).getRight();

            checkUnary(v9,Kind.RES_cos);
            Expr v10 = ((UnaryExpr)v9).getE();

            checkBinary(v10,Kind.OR);
            Expr v11 = ((BinaryExpr)v10).getLeft();

            checkBinary(v11,Kind.AND);
            Expr v12 = ((BinaryExpr)v11).getLeft();

            checkBinary(v12,Kind.LE);
            Expr v13 = ((BinaryExpr)v12).getLeft();
            checkNumLit(v13,52);Expr v14 = ((BinaryExpr)v12).getRight();

            checkBinary(v14,Kind.EXP);
            Expr v15 = ((BinaryExpr)v14).getLeft();
            checkNumLit(v15,12);Expr v16 = ((BinaryExpr)v14).getRight();

            checkBinary(v16,Kind.MINUS);
            Expr v17 = ((BinaryExpr)v16).getLeft();

            checkUnary(v17,Kind.BANG);
            Expr v18 = ((UnaryExpr)v17).getE();

            checkBinary(v18,Kind.GT);
            Expr v19 = ((BinaryExpr)v18).getLeft();

            checkBinary(v19,Kind.LT);
            Expr v20 = ((BinaryExpr)v19).getLeft();

            checkBinary(v20,Kind.GE);
            Expr v21 = ((BinaryExpr)v20).getLeft();
            checkStringLit(v21,"qwe");Expr v22 = ((BinaryExpr)v20).getRight();

            checkBinary(v22,Kind.BITOR);
            Expr v23 = ((BinaryExpr)v22).getLeft();
            checkIdent(v23,"_abc");Expr v24 = ((BinaryExpr)v22).getRight();
            checkNumLit(v24,321);Expr v25 = ((BinaryExpr)v19).getRight();
            checkStringLit(v25,"123");Expr v26 = ((BinaryExpr)v18).getRight();

            checkBinary(v26,Kind.EXP);
            Expr v27 = ((BinaryExpr)v26).getLeft();
            checkIdent(v27,"abc");Expr v28 = ((BinaryExpr)v26).getRight();
            checkNumLit(v28,462);Expr v29 = ((BinaryExpr)v16).getRight();
            checkNumLit(v29,156);Expr v30 = ((BinaryExpr)v11).getRight();

            checkBinary(v30,Kind.MINUS);
            Expr v31 = ((BinaryExpr)v30).getLeft();

            checkBinary(v31,Kind.MOD);
            Expr v32 = ((BinaryExpr)v31).getLeft();
            checkNumLit(v32,12);Expr v33 = ((BinaryExpr)v31).getRight();

            checkBinary(v33,Kind.AND);
            Expr v34 = ((BinaryExpr)v33).getLeft();
            checkNumLit(v34,1);Expr v35 = ((BinaryExpr)v33).getRight();

            checkUnary(v35,Kind.RES_atan);
            Expr v36 = ((UnaryExpr)v35).getE();
            checkNumLit(v36,0);Expr v37 = ((BinaryExpr)v30).getRight();

            checkBinary(v37,Kind.TIMES);
            Expr v38 = ((BinaryExpr)v37).getLeft();
            checkNumLit(v38,0);Expr v39 = ((BinaryExpr)v37).getRight();
            checkNumLit(v39,12);Expr v40 = ((BinaryExpr)v10).getRight();

            checkBinary(v40,Kind.MINUS);
            Expr v41 = ((BinaryExpr)v40).getLeft();
            checkNumLit(v41,8);Expr v42 = ((BinaryExpr)v40).getRight();

            checkUnary(v42,Kind.MINUS);
            Expr v43 = ((UnaryExpr)v42).getE();

            checkUnary(v43,Kind.RES_sin);
            Expr v44 = ((UnaryExpr)v43).getE();
            checkNumLit(v44,9);});
    }

    @Test
    void test47() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 --4""";
            AST ast = getAST(input);

            checkUnary(ast,Kind.MINUS);
            Expr v0 = ((UnaryExpr)ast).getE();

            checkUnary(v0,Kind.MINUS);
            Expr v1 = ((UnaryExpr)v0).getE();
            checkNumLit(v1,4);});
    }

    @Test
    void test48() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
--b""";
            AST ast = getAST(input);

            checkUnary(ast,Kind.MINUS);
            Expr v0 = ((UnaryExpr)ast).getE();

            checkUnary(v0,Kind.MINUS);
            Expr v1 = ((UnaryExpr)v0).getE();
            checkIdent(v1,"b");});
    }

    @Test
    void test49() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
1---3""";
            AST ast = getAST(input);

            checkBinary(ast,Kind.MINUS);
            Expr v0 = ((BinaryExpr)ast).getLeft();
            checkNumLit(v0,1);Expr v1 = ((BinaryExpr)ast).getRight();

            checkUnary(v1,Kind.MINUS);
            Expr v2 = ((UnaryExpr)v1).getE();

            checkUnary(v2,Kind.MINUS);
            Expr v3 = ((UnaryExpr)v2).getE();
            checkNumLit(v3,3);});
    }

    @Test
    void test50() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test51() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
b + + 2""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test52() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
3 @ 4""";
            assertThrows(LexicalException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test53() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
3 + if d ? b ? c""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test54() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
) + 5""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test55() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
if if 5 ? 1 ? 2 ? 4""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test56() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
((5 + b)""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test57() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
(atan) 5""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test58() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
** 5""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test59() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
q + """;
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test60() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
b - """;
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test61() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
c **""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test62() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
()""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test63() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
(5+-)""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test64() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
("str)" """;
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test65() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
5 + sin""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test66() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
! sin""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test67() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
(10 if 10)""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test68() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
(1 ? 1)""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test69() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 (1 ! 1)""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test70() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
 (1 ( 1)""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test71() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
if 5 - 5 - 1""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test72() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
+ 5""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test73() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
(Z 5)""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test74() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
> 5""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test75() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
| _abc""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test76() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
% 12""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test77() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
b **** c""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test78() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
10 & & 5""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test79() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
- - -""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test80() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
if + if""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test81() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
sin + sin""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

    @Test
    void test82() throws PLCException{
        assertTimeoutPreemptively(Duration.ofMillis(TIMEOUT_MILLIS), () -> {
            String input = """
32 / "str" == ident + Z & rand - cos + 52 <= 12 ** ! "qwe" >= _abc | 321 < "123" > abc ** 462 - 156 && 12 % 1 && atan + 0 - 0 * 12 || 8 --sin 9""";
            assertThrows(SyntaxException.class, () -> {
                @SuppressWarnings("unused")
                AST ast = getAST(input);
            });
        });
    }

}