#ifndef clox_table_h
#define clox_table_h

#include "common.h"
#include "value.h"

typedef struct {
    ObjString* key;
    Value value;
} Entry;

typedef struct {
    int count;
    int capacity;
    Entry* entries;
} Table;

void initTable(Table* table);
void freeTable(Table* table);
// `tableGet` retrieve the value corresponding to `key` in the `table`.
// If the `table` contains the `key`, it returns true and the `value` output parameter points to the resulting value.
// Otherwise it returns false.
bool tableGet(Table* table, ObjString* key, Value* value);
bool tableDelete(Table* table, ObjString* key);
// `tableSet` returns `true` if a new entry was added.
bool tableSet(Table* table, ObjString* key, Value value);
void tableAddAll(Table* from, Table* to);
ObjString* tableFindString(Table* table, const char* chars, int length, uint32_t hash);
void tableRemoveWhite(Table* table);
void markTable(Table* table);
#endif
