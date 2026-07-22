package com.craftinginterpreters.lox;

import static com.craftinginterpreters.lox.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class ScannerTest {

    @Test
    void testScanSingleToken() {
        record TestCase(
            String source,
            TokenType expectedType,
            String expectedLexeme,
            Object expectedLiteral
        ) {}

        var cases = List.of(
            new TestCase("(", LEFT_PAREN, "(", null),
            new TestCase(")", RIGHT_PAREN, ")", null),
            new TestCase("{", LEFT_BRACE, "{", null),
            new TestCase("}", RIGHT_BRACE, "}", null),
            new TestCase(",", COMMA, ",", null),
            new TestCase("-", MINUS, "-", null),
            new TestCase("+", PLUS, "+", null),
            new TestCase(";", SEMICOLON, ";", null),
            new TestCase("/", SLASH, "/", null),
            new TestCase("*", STAR, "*", null),

            new TestCase("!", BANG, "!", null),
            new TestCase("!=", BANG_EQUAL, "!=", null),
            new TestCase("=", EQUAL, "=", null),
            new TestCase("==", EQUAL_EQUAL, "==", null),
            new TestCase(">", GREATER, ">", null),
            new TestCase(">=", GREATER_EQUAL, ">=", null),
            new TestCase("<", LESS, "<", null),
            new TestCase("<=", LESS_EQUAL, "<=", null),

            new TestCase("a", IDENTIFIER, "a", null),
            new TestCase("varr", IDENTIFIER, "varr", null),
            new TestCase("_var", IDENTIFIER, "_var", null),

            new TestCase("\"Hello+-*/\"", STRING, "\"Hello+-*/\"", "Hello+-*/"),

            new TestCase("123", NUMBER, "123", 123.0),
            new TestCase("123.45", NUMBER, "123.45", 123.45),

            new TestCase("and", AND, "and", null),
            new TestCase("class", CLASS, "class", null),
            new TestCase("else", ELSE, "else", null),
            new TestCase("false", FALSE, "false", null),
            new TestCase("fun", FUN, "fun", null),
            new TestCase("for", FOR, "for", null),
            new TestCase("if", IF, "if", null),
            new TestCase("nil", NIL, "nil", null),
            new TestCase("or", OR, "or", null),
            new TestCase("print", PRINT, "print", null),
            new TestCase("return", RETURN, "return", null),
            new TestCase("super", SUPER, "super", null),
            new TestCase("this", THIS, "this", null),
            new TestCase("true", TRUE, "true", null),
            new TestCase("var", VAR, "var", null),
            new TestCase("while", WHILE, "while", null)
        );

        for (var testCase : cases) {
            var actual = new Scanner(testCase.source()).scanTokens().get(0);
            assertAll(
                () -> assertEquals(testCase.expectedType(), actual.type),
                () -> assertEquals(testCase.expectedLexeme(), actual.lexeme),
                () -> assertEquals(testCase.expectedLiteral(), actual.literal)
            );
        }
    }

    private record TokenValue(TokenType type, String lexeme, Object literal) {
        static TokenValue from(Token token) {
            return new TokenValue(token.type, token.lexeme, token.literal);
        }
    }

    @Test
    void testScanNumbers() {
        List<TokenValue> tokens = new Scanner("123 123. 123.456")
            .scanTokens()
            .stream()
            .limit(4)
            .map(TokenValue::from)
            .toList();

        var expected = List.of(
            new TokenValue(NUMBER, "123", 123.0),
            new TokenValue(NUMBER, "123", 123.0),
            new TokenValue(DOT, ".", null),
            new TokenValue(NUMBER, "123.456", 123.456)
        );
        assertEquals(expected, tokens);
    }

    @Test
    void testScanMultipleLineString() {
        var source = "\"abc\ndef\"";
        var token = new Scanner(source).scanTokens().get(0);
        assertEquals(token.type, STRING);
        assertEquals(token.lexeme, "\"abc\ndef\"");
        assertEquals(token.literal, "abc\ndef");
    }

    @Test
    void testScanIdentifiers() {
        List<TokenValue> tokens = new Scanner("abc _abc _1abc _1 1abc")
            .scanTokens()
            .stream()
            .limit(6)
            .map(TokenValue::from)
            .toList();

        var expected = List.of(
            new TokenValue(IDENTIFIER, "abc", null),
            new TokenValue(IDENTIFIER, "_abc", null),
            new TokenValue(IDENTIFIER, "_1abc", null),
            // _1 is valid identifier!
            new TokenValue(IDENTIFIER, "_1", null),
            new TokenValue(NUMBER, "1", 1.0),
            new TokenValue(IDENTIFIER, "abc", null)
        );
        assertEquals(expected, tokens);
    }
}
