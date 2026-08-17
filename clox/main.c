#include <stdio.h>

#include "common.h"
#include "chunk.h"
#include "debug.h"
#include "vm.h"

int main(int argc, const char* argv[]) {
    (void)argc;
    (void)argv;
    printf("hello clox!\n");

    initVM();

    Chunk chunk;
    initChunk(&chunk);

    // 添加一个常量到 constant pool 得到常量的 index。
    // 然后添加一条 OP_CONSTANT 指令。常量的 index 为指令的 operand。
    // 1.2
    int constant = addConstant(&chunk, 1.2);
    writeChunk(&chunk, OP_CONSTANT, 123);
    writeChunk(&chunk, constant, 123);

    // 3.4
    constant = addConstant(&chunk, 3.4);
    writeChunk(&chunk, OP_CONSTANT, 123);
    writeChunk(&chunk, constant, 123);

    // +
    writeChunk(&chunk, OP_ADD, 123);

    // 5.6
    constant = addConstant(&chunk, 5.6);
    writeChunk(&chunk, OP_CONSTANT, 123);
    writeChunk(&chunk, constant, 123);

    // /
    writeChunk(&chunk, OP_DIVIDE, 123);

    // -
    writeChunk(&chunk, OP_NEGATE, 123);

    writeChunk(&chunk, OP_RETURN, 123);
    disassembleChunk(&chunk, "test chunk");

    printf("\n == running ==\n");
    interpret(&chunk);
    freeVM();
    freeChunk(&chunk);

    return 0;
}
