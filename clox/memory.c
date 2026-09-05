#include <_stdlib.h>
#include <stdlib.h>

#include "memory.h"
#include "chunk.h"
#include "object.h"
#include "vm.h"

// The single function we'll use for all dynamic memory management in clox:
// allocating memory, freeing it, and changing the size of existing allocation.
// - oldSize=0, newSize!=0: Allocate new block.
// - oldSize!=0, newSize=0: Free allocation.
// - oldSize!=0, newSize<oldSize: Shrink existing allocation.
// - oldSize!=0, newSize>oldSize: Grow existing allocation.
void* reallocate(void *pointer, size_t oldSize, size_t newSize) {
    (void)oldSize;
    if (newSize == 0) {
        free(pointer);
        return NULL;
    }

    void* result = realloc(pointer, newSize);
    if (result == NULL) {
        exit(1);
    }
    return result;
}

static void freeObject(Obj* object) {
    switch (object->type) {
        case OBJ_CLOSURE: {
            // ObjClosure does not own the ObjUpvalue objects themselves,
            // but it does own the array containing pointers to those upvalues.
            ObjClosure* closure = (ObjClosure*)object;
            FREE_ARRAY(ObjUpvalue*, closure->upvalues, closure->upvalueCount);
            FREE(ObjClosure, object);
            break;
        }
        case OBJ_FUNCTION: {
            ObjFunction* function = (ObjFunction*)object;
            freeChunk(&function->chunk);
            FREE(ObjFunction, object);
            break;
        }
        case OBJ_NATIVE: {
            FREE(ObjNative, object);
            break;
        }
        case OBJ_STRING: {
            ObjString* string = (ObjString*)object;
            FREE_ARRAY(char, string->chars, string->length + 1);
            FREE(ObjString, object);
            break;
        }
        case OBJ_UPVALUE:
            FREE(ObjUpvalue, object);
            break;
    }
}

void freeObjects() {
    Obj* object = vm.objects;
    while (object != NULL) {
        Obj* next = object->next;
        freeObject(object);
        object = next;
    }
}
