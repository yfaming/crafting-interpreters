# Generate Expr.java, Stmt.java
./mvnw compile
java -cp  target/classes com.craftinginterpreters.tool.GenerateAst src/main/java/com/craftinginterpreters/lox
