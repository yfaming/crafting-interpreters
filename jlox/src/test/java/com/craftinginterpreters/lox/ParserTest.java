package com.craftinginterpreters.lox;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {
    record ParseExprTestCase(String source, String expected) {}

    private static String parseAndPrintExpr(String source) {
        var scanner = new Scanner(source);
        var tokens = scanner.scanTokens();
        var parser = new Parser(tokens);

        var expr = parser.expression();
        if (expr == null) {
            return null;
        }
        return new AstPrinter().print(expr);
    }

    @Test
    void testParseSimpleExprs() {
        var cases = List.of(
            new ParseExprTestCase("true != false", "(!= true false)"),
            new ParseExprTestCase("1 >= 2", "(>= 1.0 2.0)"),
            new ParseExprTestCase("3 - 4", "(- 3.0 4.0)"),
            new ParseExprTestCase("5 / 6", "(/ 5.0 6.0)"),
            new ParseExprTestCase("!true", "(! true)"),
            new ParseExprTestCase("-1", "(- 1.0)"),
            new ParseExprTestCase("(true)", "(group true)"),
            new ParseExprTestCase("a + b", "(+ a b)"),
            new ParseExprTestCase("a or b", "(or a b)"),
            new ParseExprTestCase("a and b", "(and a b)")
        );

        for (var testCase : cases) {
            assertEquals(parseAndPrintExpr(testCase.source()), testCase.expected());
        }
    }

    @Test
    void testParseComplexExprs() {
        var cases = List.of(
            new ParseExprTestCase("1 > 2 == false", "(== (> 1.0 2.0) false)"),
            new ParseExprTestCase("1 + 2 / 3", "(+ 1.0 (/ 2.0 3.0))"),
            new ParseExprTestCase("1 + -2", "(+ 1.0 (- 2.0))"),
            new ParseExprTestCase("true == !false", "(== true (! false))"),
            new ParseExprTestCase("(1 + 2) * 3", "(* (group (+ 1.0 2.0)) 3.0)"),
            new ParseExprTestCase("(a + b) * c", "(* (group (+ a b)) c)"),
            new ParseExprTestCase("a or b or c", "(or (or a b) c)"),
            new ParseExprTestCase("a and b and c", "(and (and a b) c)"),
            new ParseExprTestCase("a and b or c", "(or (and a b) c)")
        );

        for (var testCase : cases) {
            assertEquals(testCase.expected(), parseAndPrintExpr(testCase.source()));
        }
    }

    Stmt parseSingleStmt(String source) {
        var scanner = new Scanner(source);
        var tokens = scanner.scanTokens();
        var parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();
        return stmts.get(0);
    }

    @Test
    void testParsePrintStmt() {
        assertTrue(parseSingleStmt("print 123;") instanceof Stmt.Print);
        assertTrue(parseSingleStmt("print 1+2-3*4/5;") instanceof Stmt.Print);
        // missing `;`
        // Because of error recovery, Parser::parse() does not throw excetion.
        // It returns null instead.
        assertNull(parseSingleStmt("print 1+2-3*4/5"));
    }

    @Test
    void testExpressionStmt() {
        assertTrue(parseSingleStmt("123;") instanceof Stmt.Expression);
        assertTrue(parseSingleStmt("1+2-3*4/5;") instanceof Stmt.Expression);
        assertTrue(parseSingleStmt("123;") instanceof Stmt.Expression);
        // missing `;`
        // Because of error recovery, Parser::parse() does not throw excetion.
        // It returns null instead.
        assertNull(parseSingleStmt("1+2"));
    }

    @Test
    void testVarDeclarationStmt() {
        assertTrue(parseSingleStmt("var a;") instanceof Stmt.Var);
        assertTrue(parseSingleStmt("var a = true;") instanceof Stmt.Var);
    }

    @Test
    void testIfStmt() {
        assertTrue(parseSingleStmt("if (true) 1; else 0;") instanceof Stmt.If);
    }

    @Test
    void testWhileStmt() {
        assertTrue(parseSingleStmt("while (true) { print \"stuck\"; }") instanceof Stmt.While);
    }
}
