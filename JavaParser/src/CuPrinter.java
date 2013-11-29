import java.io.FileInputStream;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

public class CuPrinter {

    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("/Users/andrewconway/Documents/workspace/BouncingBall/src/mvc/BouncingBall.java");

        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }

        // prints the resulting AAAAAAAAAAAAAtion unit to default system output
        System.out.println(cu.toString());
    }
}