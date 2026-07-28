package com.craftinginterpreters.lox;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    record TestCase(String source, String expected) {}

    private static String parseAndPrintExpr(String source) {
        var scanner = new Scanner(source);
        var tokens = scanner.scanTokens();
        var parser = new Parser(tokens);

        var expr = parser.parse();
        if (expr == null) {
            return null;
        }
        return new AstPrinter().print(expr);
    }

    @Test
    void testParseSimpleExprs() {
        var cases = List.of(
            new TestCase("true != false", "(!= true false)"),
            new TestCase("1 >= 2", "(>= 1.0 2.0)"),
            new TestCase("3 - 4", "(- 3.0 4.0)"),
            new TestCase("5 / 6", "(/ 5.0 6.0)"),
            new TestCase("!true", "(! true)"),
            new TestCase("-1", "(- 1.0)"),
            new TestCase("(true)", "(group true)")
        );

        for (var testCase : cases) {
            assertEquals(parseAndPrintExpr(testCase.source()), testCase.expected());
        }
    }

    @Test
    void testParseComplexExprs() {
        var cases = List.of(
            new TestCase("1 > 2 == false", "(== (> 1.0 2.0) false)"),
            new TestCase("1 + 2 / 3", "(+ 1.0 (/ 2.0 3.0))"),
            new TestCase("1 + -2", "(+ 1.0 (- 2.0))"),
            new TestCase("true == !false", "(== true (! false))"),
            new TestCase("(1 + 2) * 3", "(* (group (+ 1.0 2.0)) 3.0)")
        );

        for (var testCase : cases) {
            assertEquals(parseAndPrintExpr(testCase.source()), testCase.expected());
        }
    }
}
