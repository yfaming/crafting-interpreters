#include <stdio.h>

#include "common.h"
#include "compiler.h"
#include "scanner.h"

char* tokenType2String(TokenType type) {
    switch (type) {
        case TOKEN_LEFT_PAREN: return "left_paren";
        case TOKEN_RIGHT_PAREN: return "right_paren";
        case TOKEN_LEFT_BRACE: return "left_brace";
        case TOKEN_RIGHT_BRACE: return "right_brace";
        case TOKEN_COMMA: return "comma";
        case TOKEN_DOT: return "dot";
        case TOKEN_MINUS: return "minus";
        case TOKEN_PLUS: return "plus";
        case TOKEN_SEMICOLON: return "semicolon";
        case TOKEN_SLASH: return "slash";
        case TOKEN_STAR: return "star";
        //
        case TOKEN_BAN: return "ban";
        case TOKEN_BANG_EQUAL: return "ban_equal";
        case TOKEN_EQUAL: return "equal";
        case TOKEN_EQUAL_EQUAL: return "equal_equal";
        case TOKEN_GREATER: return "greater";
        case TOKEN_GREATER_EQUAL: return "greater_equal";
        case TOKEN_LESS: return "less";
        case TOKEN_LESS_EQUAL: return "less_equal";
        //
        case TOKEN_IDENTIFIER: return "identifier";
        case TOKEN_STRING: return "string";
        case TOKEN_NUMBER: return "number";
        //
        case TOKEN_AND: return "and";
        case TOKEN_CLASS: return "class";
        case TOKEN_ELSE: return "else";
        case TOKEN_FALSE: return "false";
        case TOKEN_FOR: return "for";
        case TOKEN_FUN: return "fun";
        case TOKEN_IF: return "if";
        case TOKEN_NIL: return "nil";
        case TOKEN_OR: return "or";
        case TOKEN_PRINT: return "print";
        case TOKEN_RETURN: return "return";
        case TOKEN_SUPER: return "super";
        case TOKEN_THIS: return "this";
        case TOKEN_TRUE: return "true";
        case TOKEN_VAR: return "var";
        case TOKEN_WHILE: return "while";
        //
        case TOKEN_ERROR: return "error";
        case TOKEN_EOF: return "eof";
        default: return "unreachable";
    }
}

void compile(const char* source) {
    initScanner(source);
    int line = -1;
    for (;;) {
        Token token = scanToken();
        if (token.line != line) {
            printf("%4d ", token.line);
            line = token.line;
        } else {
            printf("   | ");
        }
        printf("%s '%.*s'\n", tokenType2String(token.type), token.length, token.start);

        if (token.type == TOKEN_EOF) {
            break;;
        }
    }
}
