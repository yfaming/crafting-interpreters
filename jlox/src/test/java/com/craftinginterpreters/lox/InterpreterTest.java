package com.craftinginterpreters.lox;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

public class InterpreterTest {

    Object evaluate(String source) {
        var scanner = new Scanner(source);
        var tokens = scanner.scanTokens();
        var parser = new Parser(tokens);
        var expr = parser.parse();
        var interpreter = new Interpreter();
        return interpreter.evaluate(expr);
    }

    record TestCase(String source, Object expected) {}

    void runTestCases(List<TestCase> testCases) {
        for (var testCase : testCases) {
            assertEquals(evaluate(testCase.source()), testCase.expected);
        }
    }

    @Test
    void testEvaluateLiteral() {
        runTestCases(
            List.of(
                new TestCase("true", true),
                new TestCase("false", false),
                new TestCase("1", 1.0),
                new TestCase("\"hello, world!\"", "hello, world!")
            )
        );
    }

    @Test
    void testEvaluateUnaryExpr() {
        runTestCases(
            List.of(
                new TestCase("-1", -1.0),
                new TestCase("!true", false),
                new TestCase("!false", true)
            )
        );
    }

    @Test
    void testEvaluateBinaryExpr() {
        runTestCases(
            List.of(
                new TestCase("1 + 2", 3.0),
                new TestCase("1 - 2", -1.0),
                new TestCase("3 * 4", 12.0),
                new TestCase("3 / 4", 0.75),

                new TestCase("\"hello \" + \"world\"", "hello world"),

                new TestCase("1 > 2", false),
                new TestCase("1 >= 2", false),
                new TestCase("2 > 1", true),
                new TestCase("2 >= 1", true),

                new TestCase("1 < 2", true),
                new TestCase("1 <= 2", true),
                new TestCase("2 < 1", false),
                new TestCase("2 <= 1", false),

                new TestCase("1 != 1", false),
                new TestCase("true != true", false),
                new TestCase("\"hello\" != \"hello\"", false),
                new TestCase("nil != nil", false),

                new TestCase("1 != 2", true),
                new TestCase("1 != \"1\"", true),
                new TestCase("1 != true", true),
                new TestCase("1 != false", true),
                new TestCase("\"1\" != true", true),
                new TestCase("1 == 1", true),
                new TestCase("1 == 2", false),
                new TestCase("\"1\" == \"1\"", true)
            )
        );
    }

    @Test
    void testEvaluateInvalidExpr() {
        var inputs = List.of("-true", "1 + \"whaaat\"", "2 < \"3\"");
        for (var input : inputs) {
            assertThrows(RuntimeError.class, () -> evaluate(input));
        }
    }
}
