# Compile and run jlox interpreter.
./mvnw compile
exec java -cp target/classes com.craftinginterpreters.lox.Lox "$@"
