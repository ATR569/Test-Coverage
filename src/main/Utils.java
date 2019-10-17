package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

/**
 *
 * @author Adson Macêdo
 * @author Thairam Michel
 */
public abstract class Utils {
    public static void resetLog(String logFile) throws IOException{
        FileWriter fileW = new FileWriter(logFile);
        PrintWriter printW = new PrintWriter(fileW);
        printW.printf("");
        fileW.close();
    }

    public static String getFileAsString(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String lines = "";
        
        while (br.ready()) {
            lines += '\n' + br.readLine();
        }

        br.close();
        return lines;
    }

    public static void saveToFile(String filePath, String program) throws IOException{
        FileWriter fileW = new FileWriter(filePath);
        PrintWriter printW = new PrintWriter(fileW);
        printW.printf(program);
        fileW.close();
    }
    
    public static ImportDeclaration getImportInstance(AST ast, String sourceImport, boolean onDemand){
        String [] source = sourceImport.split("[.]");
        ImportDeclaration impDecl = ast.newImportDeclaration();
        impDecl.setName(ast.newName(source));
        impDecl.setOnDemand(onDemand);
        
        return impDecl;
    }
    
    public static PackageDeclaration getPackageInstance(AST ast, String packageName){
        PackageDeclaration packDecl = ast.newPackageDeclaration();
        packDecl.setName(ast.newSimpleName(packageName));
        
        return packDecl;
    }
    
    public static ExpressionStatement getMethodInvocationStmt(AST ast, String expression, String methodName, String [] args, boolean [] isLiteral){
        MethodInvocation methodInv = ast.newMethodInvocation();
        
        
        if (expression != null) {
            String [] exp = expression.split("[.]");
            methodInv.setExpression(ast.newName(exp));
        }
        
        methodInv.setName(ast.newSimpleName(methodName));

        if (args != null && isLiteral != null){
            for (int i = 0; i < args.length; i++){
                if (isLiteral[i]){
                    StringLiteral literal = ast.newStringLiteral();
                    literal.setLiteralValue(args[i]);
                    methodInv.arguments().add(literal);
                }else{
                    methodInv.arguments().add(ast.newSimpleName(args[i]));
                }
            }
        }
        
        return ast.newExpressionStatement(methodInv);
    }
    
    public static ClassInstanceCreation getClassCreation(AST ast, String type, String [] args, String [] argTypes){
        ClassInstanceCreation classInstance = ast.newClassInstanceCreation();
        classInstance.setType(ast.newSimpleType(ast.newSimpleName(type)));
        
        for (int i = 0; i < args.length; i++) {
            
            switch (argTypes[i]) {
                case "name":
                    classInstance.arguments().add(ast.newSimpleName(args[i]));
                    break;
                case "bool":
                    classInstance.arguments().add(ast.newBooleanLiteral(args[i].equals("true")));
                    break;
                case "String":
                    StringLiteral literal = ast.newStringLiteral();
                    literal.setLiteralValue(args[i]);
                    classInstance.arguments().add(literal);
                    break;
                default:
                    break;
            }
        }
        
        return classInstance;
    }
    
    public static ExpressionStatement getClasstAssignStmtInstance(AST ast, String name, String className, String [] args, String [] argTypes){
        Assignment assignment = ast.newAssignment();
        
        assignment.setLeftHandSide(ast.newSimpleName(name));
        assignment.setOperator(Assignment.Operator.ASSIGN);
        
        ClassInstanceCreation cic = getClassCreation(ast, className, args, argTypes);
                
        assignment.setRightHandSide(cic);
                
        return ast.newExpressionStatement(assignment);
    }
    
    public static VariableDeclarationStatement getVarStatementInstance(AST ast, String name, String type){
        VariableDeclarationFragment varDeclFrgmt = ast.newVariableDeclarationFragment();
        varDeclFrgmt.setName(ast.newSimpleName(name));
        
        VariableDeclarationStatement varDeclStmt = ast.newVariableDeclarationStatement(varDeclFrgmt);
        varDeclStmt.setType(ast.newSimpleType(ast.newSimpleName(type)));
        
        return varDeclStmt;
    }
    
    public static TryStatement getTryStatementInstance(AST ast, String []catchNames){
        TryStatement tryStmt = ast.newTryStatement();
        
        for (String name : catchNames){
            CatchClause catchClause = ast.newCatchClause();
            SingleVariableDeclaration var = ast.newSingleVariableDeclaration();
            var.setType(ast.newSimpleType(ast.newSimpleName(name)));
            var.setName(ast.newSimpleName("ex"));
            catchClause.setException(var);
            tryStmt.catchClauses().add(catchClause);
        }
        
        return tryStmt;
    }
    
    public static MethodDeclaration getMethodDeclarationInstance(AST ast, String name, ModifierKeyword [] modifiers, Type returnType, String [] args, String [] argTypes){
        //  Nome, tipo e modificador do método
        MethodDeclaration methodDecl = ast.newMethodDeclaration();
        methodDecl.setName(ast.newSimpleName(name));
        methodDecl.setReturnType2(returnType);
        methodDecl.setBody(ast.newBlock());
        
        for (ModifierKeyword modifier : modifiers) {
            methodDecl.modifiers().add(ast.newModifier(modifier));
        }
        
        //  Parâmetros do método
        for (int i = 0; i < args.length; i++){
            SingleVariableDeclaration var = ast.newSingleVariableDeclaration();
            var.setType(ast.newSimpleType(ast.newSimpleName(argTypes[i])));
            var.setName(ast.newSimpleName(args[i]));
            methodDecl.parameters().add(var);                
        }
        
        return methodDecl;
    }
}
