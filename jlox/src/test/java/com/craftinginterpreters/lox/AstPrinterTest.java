package com.craftinginterpreters.lox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class AstPrinterTest {

    record TestCase(Expr expr, String output) {}

    @Test
    void testExprPrinter() {
        var cases = List.of(
            new TestCase(new Expr.Literal(123), "123"),
            new TestCase(new Expr.Literal("abc"), "abc"),
            new TestCase(
                new Expr.Unary(
                    new Token(TokenType.MINUS, "-", null, 1),
                    new Expr.Literal(456)
                ),
                "(- 456)"
            ),
            new TestCase(
                new Expr.Binary(
                    new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)
                    ),
                    new Token(TokenType.STAR, "*", null, 1),
                    new Expr.Grouping(new Expr.Literal(45.67))
                ),
                "(* (- 123) (group 45.67))"
            ),
            new TestCase(
                new Expr.Variable(
                    new Token(TokenType.VAR, "var_name", null, 1)
                ),
                "var_name"
            ),
            new TestCase(
                new Expr.Binary(
                    new Expr.Variable(new Token(TokenType.VAR, "a", null, 1)),
                    new Token(TokenType.PLUS, "+", null, 1),
                    new Expr.Literal(1)
                ),
                "(+ a 1)"
            ),
            new TestCase(
                new Expr.Assign(
                    new Token(TokenType.IDENTIFIER, "a", null, 1),
                    new Expr.Literal(1)
                ),
                "(set! a 1)"
            ),
            // add(1, 2, 3)
            new TestCase(
                new Expr.Call(
                    new Expr.Variable(
                        new Token(TokenType.IDENTIFIER, "add", null, 1)
                    ),
                    new Token(TokenType.RIGHT_PAREN, ")", null, 1),
                    List.of(
                        new Expr.Literal(1),
                        new Expr.Literal(2),
                        new Expr.Literal(3)
                    )
                ),
                "(add 1 2 3)"
            )
        );
        for (var testCase : cases) {
            assertEquals(
                testCase.output(),
                new AstPrinter().print(testCase.expr())
            );
        }
    }
}
