import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class CodeStandards extends CBaseVisitor<String[]>{
    /**
     * Holds the Regular expressions for Struct, Union, Enum declarations and function definition.
     */
    public JSONObject declareObject = new JSONObject();

    /**
     * Holds the Regular expressions for Global variables.
     */
    public JSONObject globalObject = new JSONObject();

    /**
     * Holds the Regular expressions for Global variables.
     */
    public JSONObject localObject = new JSONObject();

    CodeStandards(JSONObject declareObject, JSONObject globalObject, JSONObject localObject){
        this.declareObject = declareObject;
        this.globalObject = globalObject;
        this.localObject = localObject;
    }


    /**
     * \brief
     *  This function visits a function definition and checks if its identifier follows the standards.
     *
     * \details
     *  This function is automatically called from the parse tree. It checks if the identifier of the function follows
     *  the rules that were predefined in <b>CStandards.json</b> file.
     *
     * \pre
     *  The user inputs a correct grammar regarding the syntax.
     *
     * \post
     *  If the function identifier doesn't follow the predefined rules, this function should print an error statement to
     *  inform the developer that the identifier doesn't follow the standards.
     *
     * \param
     *  ctx the parse tree
     */
    //function
    @Override
    public String[] visitExternalFunctionDefinition(CParser.ExternalFunctionDefinitionContext ctx) {

        String type = ctx.functionDefinition().declarationSpecifiers().declarationSpecifier(0).getText();
        String id = ctx.functionDefinition().declarator().directDeclarator().directDeclarator().getText();

        //System.out.println("function, return type: "+ type + ", id:" + id);

        if(!id.matches((String) declareObject.get("api"))){
            System.out.println("Line " + ctx.start.getLine() + ": Function \"" + id + "\" should follow the pattern: " + (String) declareObject.get("api"));
        }


        visitChildren(ctx);

        String[] s = {ctx.getText()};
        return s;
    }

    //function parameters
//    @Override
//    public String[] visitParameterDeclaration(CParser.ParameterDeclarationContext ctx) {
//
//        String type = ctx.declarationSpecifiers().declarationSpecifier(0).getText();
//        String id = ctx.declarator().getText();
//
//        //System.out.println("Function parameter, type: " + type + ", id:" + id);
//
//        String[] s = {ctx.getText()};
//        return s;
//    }


    /**
     * \brief
     *  This function visits global declarations and checks if their identifiers follow the standards.
     *
     * \details
     *  - This function is automatically called from the parse tree. It checks if the identifiers of the global declarations
     *  follow the rules that were predefined in <b>CStandards.json</b> file.
     *  - If the declaration is Enum type, call function visitTypeSpecifierEnum() which returns the type and identifier.
     *  - If the declaration is Struct or union type, call function visitTypeSpecifierStructOrUnion() which returns the type and identifier.
     *
     * \pre
     *  The user inputs a correct grammar regarding the syntax.
     *
     * \post
     *  If the global identifier doesn't follow the predefined rules, this function should print an error statement to
     *  inform the developer that the identifier doesn't follow the standards.
     *
     * \param
     *  ctx the parse tree
     *
     * \see visitTypeSpecifierEnum()
     * \see visitTypeSpecifierStructOrUnion()
     */
    //externalDeclaration: declaration
    @Override
    public String[] visitGlobalDeclaration(CParser.GlobalDeclarationContext ctx) {
        //System.out.println("Global declaration " + ctx.getText() + " in line: " + ctx.start.getLine());
        CParser.DeclarationSpecifiersContext declarationSpecifiers = ctx.declaration().declarationSpecifiers();
        int numOfdeclarationSpecifiers = declarationSpecifiers.getChildCount();

        //Global variable
        if(numOfdeclarationSpecifiers == 2){
            String type = declarationSpecifiers.declarationSpecifier(0).getText();
            String id = declarationSpecifiers.declarationSpecifier(1).getText();

            if(type.length() > 4 && type.substring(0,4).equals("enum") && !id.matches((String) globalObject.get("union"))){
                System.out.println("Line " + ctx.start.getLine() + ": Enum " + type.substring(4) + " \"" + id + "\" should follow the pattern: " + (String) globalObject.get("enum"));
            } else if(type.length() > 5 && type.substring(0,5).equals("union") && !id.matches((String) globalObject.get("union"))){
                System.out.println("Line " + ctx.start.getLine() + ": Union " + type.substring(5) + " \"" + id + "\" should follow the pattern: " + (String) globalObject.get("union"));
            } else if(type.length() > 6 && type.substring(0,6).equals("struct") && !id.matches((String) globalObject.get("struct"))){
                System.out.println("Line " + ctx.start.getLine() + ": Struct " + type.substring(6) + " \"" + id + "\" should follow the pattern: " + (String) globalObject.get("struct"));
            } else if(!id.matches((String) globalObject.get("other"))){
                System.out.println("Line " + ctx.start.getLine() + ": " + type + " \"" + id + "\" should follow the pattern: " + (String) globalObject.get("other"));
            }
            //System.out.println("Global, type: " + type + ", id: " + id);

        }
        //Struct, union or enum declaration
        else if(numOfdeclarationSpecifiers == 1){
            String[] typeAndID = visit(declarationSpecifiers.declarationSpecifier(0).typeSpecifier());
            //System.out.println("Data structure, type: " + typeAndID[0] + ", id: " + typeAndID[1]);

            if(!typeAndID[1].matches((String) declareObject.get(typeAndID[0]))){
                System.out.println("Line " + ctx.start.getLine() + ": " + typeAndID[0]+ " \"" + typeAndID[1] + "\" should follow the pattern: " + (String) declareObject.get(typeAndID[0]));
            }

        }

        String[] s = {ctx.getText()};
        return s;
    }



    /**
     * \brief
     *  This function visits global declarations and checks if their identifiers follow the standards.
     *
     * \details
     *  This function is automatically called from the parse tree. It checks if the identifiers of the global declarations
     *  follow the rules that were predefined in <b>CStandards.json</b> file.
     *
     * \pre
     *  The user inputs a correct grammar regarding the syntax.
     *
     * \post
     *  If the global identifier doesn't follow the predefined rules, this function should print an error statement to
     *  inform the developer that the identifier doesn't follow the standards.
     *
     * \param
     *  ctx the parse tree
     */
    //blockItem: declaration
    @Override
    public String[] visitLocalDeclaration(CParser.LocalDeclarationContext ctx) {
        //System.out.println("Local declaration " + ctx.getText() + " in line: " + ctx.start.getLine());
        CParser.DeclarationSpecifiersContext declarationSpecifiers = ctx.declaration().declarationSpecifiers();
        int numOfdeclarationSpecifiers = declarationSpecifiers.getChildCount();


        if(numOfdeclarationSpecifiers == 2){
            String type = declarationSpecifiers.declarationSpecifier(0).getText();
            String id = declarationSpecifiers.declarationSpecifier(1).getText();

            if(type.length() > 4 && type.substring(0,4).equals("enum") && !id.matches((String) localObject.get("union"))){
                System.out.println("Line " + ctx.start.getLine() + ": Enum " + type.substring(4) + " \"" + id + "\" should follow the pattern: " + (String) localObject.get("enum"));
            } else if(type.length() > 5 && type.substring(0,5).equals("union") && !id.matches((String) localObject.get("union"))){
                System.out.println("Line " + ctx.start.getLine() + ": Union " + type.substring(5) + " \"" + id + "\" should follow the pattern: " + (String) localObject.get("union"));
            } else if(type.length() > 6 && type.substring(0,6).equals("struct") && !id.matches((String) localObject.get("struct"))){
                System.out.println("Line " + ctx.start.getLine() + ": Struct " + type.substring(6) + " \"" + id + "\" should follow the pattern: " + (String) localObject.get("struct"));
            } else if(!id.matches((String) localObject.get("other"))){
                System.out.println("Line " + ctx.start.getLine() + ": " + type + " \"" + id + "\" should follow the pattern: " + (String) localObject.get("other"));
            }
            //System.out.println("local, type: " + type + ", id: " + id);
        }

        String[] s = {ctx.getText()};
        return s;
    }


    /**
     * \brief
     *  This function visits TypeSpecifierEnum
     *
     * \details
     *  This function is called from visitGlobalDeclaration(). It returns an array of strings which contains the
     *  type (enum) and the identifier.
     *
     * \param
     *  ctx the parse tree
     *
     * \return
     *  array of strings which contains the type (enum) and the identifier.
     */
    @Override
    public String[] visitTypeSpecifierEnum(CParser.TypeSpecifierEnumContext ctx) {

        String type = ctx.enumSpecifier().Enum().getText();
        String id = ctx.enumSpecifier().Identifier().getText();
        String[] typeAndName = {type,id};
        return typeAndName;
    }



    /**
     * \brief
     *  This function visits TypeSpecifierStructOrUnion
     *
     * \details
     *  This function is called from visitGlobalDeclaration(). It returns an array of strings which contains the
     *  type (struct or union) and the identifier.
     *
     * \param
     *  ctx the parse tree
     *
     * \return
     *  array of strings which contains the type (struct or union) and the identifier.
     */
    @Override
    public String[] visitTypeSpecifierStructOrUnion(CParser.TypeSpecifierStructOrUnionContext ctx) {
        String type = ctx.structOrUnionSpecifier().structOrUnion().getText();
        String id = ctx.structOrUnionSpecifier().Identifier().getText();
        String[] typeAndName = {type,id};
        return typeAndName;
    }

}
