import java.io.FileInputStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.regex.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class main {

    /*! \mainpage Compilers project Documentation
     *
     * \section About
     *
     *  This project is a toolkit for C language. It adds new standards for the code by specifying new spelling for declarations.
     * <br><br>
     *  The declaration standards for the global, local and function declaration are stored in <b>Cstandards.json</b> which are
     *  written as regex. The visitors function is used to visit the parse tree to get the declared words and its type
     *  from the input code.  Then our function compares the declared word spelling according to its type, with the regex
     *  in Cstandards.json. If it doesn’t match the regex an error message will be displayed inquiring to follow the
     *  given expression standards.
     *
     */

    /**
     * \brief
     *  This function controls the whole program.
     *
     * \details
     *  This function is responsible for:
     *  <ul>
     *      <li>Reading the C code from <b>C.expr</b> file.</li>
     *      <li>Reading the declaration rules from <b>CStandards.json</b> file.</li>
     *      <li>Generating a parse tree for the C code.</li>
     *      <li>Visiting the parse tree.</li>
     *  </ul>
     */

    public static void main(String[] args) throws Exception
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        JSONObject declare = new JSONObject();
        JSONObject declareObject = new JSONObject();
        JSONObject globalObject = new JSONObject();
        JSONObject localObject = new JSONObject();

        try (FileReader reader = new FileReader("CStandards.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
//            //array to obj to obj
//            JSONArray declarelist = (JSONArray) obj;// System.out.println(declarelist);
//            JSONObject declare =(JSONObject) declarelist.get(1);
//            JSONObject declareObject = (JSONObject) declare.get("declare");
            //obj to arr to obj
            declare = (JSONObject) obj;// System.out.println(declare);
            declareObject = (JSONObject) declare.get("declare");
            globalObject = (JSONObject) declare.get("global");
            localObject = (JSONObject) declare.get("local");
            //pattern from json file
            //Pattern pattern= Pattern.compile( (String) declareObject.get("struct"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String inputFile = "c.expr";
        FileInputStream is = new FileInputStream(inputFile);
        ANTLRInputStream input = new ANTLRInputStream(is);
        CLexer lexer = new CLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CParser parser = new CParser(tokens);
        ParseTree tree = parser.compilationUnit();

        //System.out.println(tree.toStringTree(parser));

//        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
//        parseTreeWalker.walk(new UnicodeTranslator(), tree);

        //For visitors
        CodeStandards codeStandards = new CodeStandards(declareObject,globalObject,localObject);
        codeStandards.visit(tree);



    }
}
