package com.craftinginterpreters.lox;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

public class InterpreterTest {

    Object evaluateExpr(String source) {
        var scanner = new Scanner(source);
        var tokens = scanner.scanTokens();
        var parser = new Parser(tokens);
        var expr = parser.expression();
        var interpreter = new Interpreter();
        return interpreter.evaluate(expr);
    }

    record EvaluateExprTestCase(String source, Object expected) {}

    void runEvaluateExprTestCases(List<EvaluateExprTestCase> testCases) {
        for (var testCase : testCases) {
            assertEquals(evaluateExpr(testCase.source()), testCase.expected);
        }
    }

    @Test
    void testEvaluateLiteral() {
        runEvaluateExprTestCases(
            List.of(
                new EvaluateExprTestCase("true", true),
                new EvaluateExprTestCase("false", false),
                new EvaluateExprTestCase("1", 1.0),
                new EvaluateExprTestCase("\"hello, world!\"", "hello, world!")
            )
        );
    }

    @Test
    void testEvaluateUnaryExpr() {
        runEvaluateExprTestCases(
            List.of(
                new EvaluateExprTestCase("-1", -1.0),
                new EvaluateExprTestCase("!true", false),
                new EvaluateExprTestCase("!false", true)
            )
        );
    }

    @Test
    void testEvaluateBinaryExpr() {
        runEvaluateExprTestCases(
            List.of(
                new EvaluateExprTestCase("1 + 2", 3.0),
                new EvaluateExprTestCase("1 - 2", -1.0),
                new EvaluateExprTestCase("3 * 4", 12.0),
                new EvaluateExprTestCase("3 / 4", 0.75),

                new EvaluateExprTestCase(
                    "\"hello \" + \"world\"",
                    "hello world"
                ),

                new EvaluateExprTestCase("1 > 2", false),
                new EvaluateExprTestCase("1 >= 2", false),
                new EvaluateExprTestCase("2 > 1", true),
                new EvaluateExprTestCase("2 >= 1", true),

                new EvaluateExprTestCase("1 < 2", true),
                new EvaluateExprTestCase("1 <= 2", true),
                new EvaluateExprTestCase("2 < 1", false),
                new EvaluateExprTestCase("2 <= 1", false),

                new EvaluateExprTestCase("1 != 1", false),
                new EvaluateExprTestCase("true != true", false),
                new EvaluateExprTestCase("\"hello\" != \"hello\"", false),
                new EvaluateExprTestCase("nil != nil", false),

                new EvaluateExprTestCase("1 != 2", true),
                new EvaluateExprTestCase("1 != \"1\"", true),
                new EvaluateExprTestCase("1 != true", true),
                new EvaluateExprTestCase("1 != false", true),
                new EvaluateExprTestCase("\"1\" != true", true),
                new EvaluateExprTestCase("1 == 1", true),
                new EvaluateExprTestCase("1 == 2", false),
                new EvaluateExprTestCase("\"1\" == \"1\"", true)
            )
        );
    }

    @Test
    void testEvaluateInvalidExpr() {
        var inputs = List.of("-true", "1 + \"whaaat\"", "2 < \"3\"");
        for (var input : inputs) {
            assertThrows(RuntimeError.class, () -> evaluateExpr(input));
        }
    }

    Object executeAndEvaluateExpr(String code, String expr) {
        // execute
        var interpreter = new Interpreter();
        List<Stmt> stmts = new Parser(new Scanner(code).scanTokens()).parse();
        interpreter.interpret(stmts);
        // evaluate
        return interpreter.evaluate(
            new Parser(new Scanner(expr).scanTokens()).expression()
        );
    }

    record ExecuteAndEvaluateTestCase(
        String code,
        String expr,
        Object expected
    ) {}

    void runExecuteAndEvaluateExprTestCases(
        List<ExecuteAndEvaluateTestCase> testCases
    ) {
        for (var testCase : testCases) {
            assertEquals(
                executeAndEvaluateExpr(testCase.code(), testCase.expr()),
                testCase.expected
            );
        }
    }

    @Test
    void testEvaluateVarDeclaration() {
        runExecuteAndEvaluateExprTestCases(
            List.of(
                new ExecuteAndEvaluateTestCase("var a;", "a", null),
                new ExecuteAndEvaluateTestCase("var a = 1 + 2;", "a", 3.0),
                new ExecuteAndEvaluateTestCase("var a = 2; var b = 3;", "a*b", 6.0)
            )
        );
    }

    @Test
    void testAssignment() {
        runExecuteAndEvaluateExprTestCases(
            List.of(
                new ExecuteAndEvaluateTestCase("var a; a = true;", "a", true),
                new ExecuteAndEvaluateTestCase("var a = 1; a = 2;", "a", 2.0),
                new ExecuteAndEvaluateTestCase("var a = 1; var a = 2;", "a", 2.0)
            )
        );
    }

    @Test
    void testWhileStmt() {
        runExecuteAndEvaluateExprTestCases(
            List.of(
                new ExecuteAndEvaluateTestCase("var sum=0; var i=1; while (i<=100) { sum=sum+i; i=i+1; }", "sum", 5050.0)
            )
        );
    }

    @Test
    void testForStmt() {
        runExecuteAndEvaluateExprTestCases(
            List.of(
                new ExecuteAndEvaluateTestCase("var sum=0; for (var i=1; i<=100; i=i+1) { sum = sum + i; }", "sum", 5050.0)
            )
        );

    }
}
