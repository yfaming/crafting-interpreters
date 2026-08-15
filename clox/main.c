#include <stdio.h>

#include "common.h"
#include "chunk.h"
#include "debug.h"

int main(int argc, const char* argv[]) {
    (void)argc;
    (void)argv;
    printf("hello clox!\n");

    Chunk chunk;
    initChunk(&chunk);

    // 添加一个常量到 constant pool 得到常量的 index。
    // 然后添加一条 OP_CONSTANT 指令。常量的 index 为指令的 operand。
    int constant = addConstant(&chunk, 1.2);
    writeChunk(&chunk, OP_CONSTANT, 123);
    writeChunk(&chunk, constant, 123);

    writeChunk(&chunk, OP_RETURN, 123);
    disassembleChunk(&chunk, "test chunk");
    freeChunk(&chunk);

    return 0;
}
