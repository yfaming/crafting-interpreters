#ifndef clox_value_h
#define clox_value_h

#include "common.h"

typedef double Value;

typedef struct {
    int capacity;
    int count;
    Value* values;
} ValueArry;

void initValueArray(ValueArry* array);
void writeValueArray(ValueArry* array, Value value);
void freeValueArray(ValueArry* array);
void printValue(Value value);

#endif
