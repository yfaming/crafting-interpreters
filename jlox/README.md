jlox
====

Java implementation of the Lox interpreter from [Crafting Interpreters](https://craftinginterpreters.com/).

This project uses [Maven](https://maven.apache.org/) to manage the Java code. Most of the
interpreter closely follows the implementation in the book. The tests are my own.


## Run

Build and run the jlox interpreter:

```sh
./jlox.sh
```

## Test

The following commands demonstrate several ways to run the tests:

```sh
# Run all tests.
./mvnw test

# Run a specific test class
./mvnw -Dtest=ScannerTest test

# Run a specific test method
./mvnw -Dtest=ScannerTest#TestScanSingleToken test

# Run multiple test classes
./mvnw -Dtest='ScannerTest,ParserTest' test
```
