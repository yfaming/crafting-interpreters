#ifndef clox_vm_h
#define clox_vm_h

#include "chunk.h"
#include "table.h"
#include "value.h"

#define  STACK_MAX 2560

typedef struct {
    Chunk* chunk;
    // `ip` points to the next byte of code to use executed.
    uint8_t* ip;
    Value stack[STACK_MAX];
    // `stackTop` points just past the top item.
    // Or, `stackTop` points to where the next value to be pushed will go.
    Value* stackTop;
    Table strings;
    Obj* objects;
} VM;

typedef enum {
    INTERPRET_OK,
    INTERPRET_COMPILE_ERROR,
    INTERPRET_RUNTIME_ERROR
} InterpretResult;

extern VM vm;

void initVM();
void freeVM();

InterpretResult interpret(const char* source);
void push(Value value);
Value pop();

#endif
