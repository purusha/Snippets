using javax.tools.JavaCompiler;

<dependency>
    <groupId>org.mdkt.compiler</groupId>
    <artifactId>InMemoryJavaCompiler</artifactId>
    <version>1.3.0</version>
</dependency>

StringBuilder sourceCode = new StringBuilder();
sourceCode.append("package org.mdkt;\n");
sourceCode.append("public class HelloClass {\n");
sourceCode.append("   public String hello() { return \"hello\"; }");
sourceCode.append("}");

Class<?> helloClass = InMemoryJavaCompiler.newInstance().compile("org.mdkt.HelloClass", sourceCode.toString());

see https://stackoverflow.com/questions/12173294/compile-code-fully-in-memory-with-javax-tools-javacompiler
for other ideas ...
