package main;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Adson Macêdo
 * @author Thairam Michel
 */
public class CoverageTransform {

    public static Map<String, Set<String>> parseFiles(String sourcePath, String targetPath) {
        File[] files = new File(sourcePath).listFiles();
        Map<String, Set<String>> result = new HashMap<>();

        for (File f : files) {
            if (f.isFile()) {
                try {
                    String fileName = f.getName();
                    String target = targetPath + "\\" + f.getName();

                    System.out.println("Processando: " + fileName);
                    CompilationUnit unitTarget = processFile(fileName, sourcePath, result);

                    System.out.println("Salvando: " + target);
                    Utils.saveToFile(target, unitTarget.toString());

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        return result;
    }

    private static void addSaveMethod(CompilationUnit unit) {
        AST ast = unit.getAST();

        //  private void saveLog(String log){
        MethodDeclaration methodDecl = Utils.getMethodDeclarationInstance(ast, "saveLog",
                new ModifierKeyword[]{ModifierKeyword.PRIVATE_KEYWORD},
                ast.newPrimitiveType(PrimitiveType.VOID),
                new String[]{"log"},
                new String[]{"String"});

        //  try{
        TryStatement tryStmt = Utils.getTryStatementInstance(ast, new String[]{"IOException"});
        //FileWriter fileW;
        tryStmt.getBody().statements().add(0, Utils.getVarStatementInstance(ast, "fileW", "FileWriter"));
        //PrintWriter printW;
        tryStmt.getBody().statements().add(0, Utils.getVarStatementInstance(ast, "printW", "PrintWriter"));
        //fileW = new FileWriter("log.txt");
        tryStmt.getBody().statements().add(Utils.getClasstAssignStmtInstance(ast, "fileW", "FileWriter", new String[]{"log.txt", "true"}, new String[]{"String", "bool"}));
        //printW = new PrintWriter(fileW);
        tryStmt.getBody().statements().add(Utils.getClasstAssignStmtInstance(ast, "printW", "PrintWriter", new String[]{"fileW"}, new String[]{"name"}));
        //  printW.printf(log);
        tryStmt.getBody().statements().add(Utils.getMethodInvocationStmt(ast, "printW", "println", new String[]{"log"}, new boolean[]{false}));
        //  fileW.close()
        tryStmt.getBody().statements().add(Utils.getMethodInvocationStmt(ast, "fileW", "close", null, null));
        //  } catch (IOException exIOException){ }

        methodDecl.getBody().statements().add(tryStmt);

        //  Adiciona o método na unit
        TypeDeclaration types = (TypeDeclaration) unit.types().get(0);
        types.bodyDeclarations().add(methodDecl);
    }

    /**
     * Processa todos o conteúdo de source
     *
     * @param sourceFile
     * @return String com o target
     */
    private static CompilationUnit processFile(String sourceFile, String path, Map<String, Set<String>> result) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS8);

        //  Configurações para resolver o problema do enum
        Map options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
        parser.setCompilerOptions(options);

        parser.setSource(Utils.getFileAsString(path + "\\" + sourceFile).toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        AST ast = unit.getAST();

        //  Altera o package pra target
        unit.setPackage(Utils.getPackageInstance(ast, "target"));

        // Visitors
        ASTVisitor visitor = new SmartASTVisitor() {
            private void addLogCall(String log, ASTNode node) {
                if (node instanceof MethodDeclaration) {
                    ((MethodDeclaration)node).getBody().statements().add(0, Utils.getMethodInvocationStmt(ast, null, "saveLog", new String[]{log}, new boolean[]{true}));
                } else {
                    while (!(node.getParent() instanceof Block)) {
                        node = node.getParent();
                    }

                    Block body = (Block) node.getParent();

                    int pos = ((Block) body).statements().indexOf(node);
                    if (pos < 0) {
                        pos = 0;
                    }

                    body.statements().add(pos, Utils.getMethodInvocationStmt(ast, null, "saveLog", new String[]{log}, new boolean[]{true}));
                }
            }

            @Override
            public boolean visit(MethodDeclaration node) {
                SimpleName name = node.getName();
                String log = sourceFile + ":methodDeclaration:" + name.getIdentifier() + " - line: " + unit.getLineNumber(node.getStartPosition());
                this.names.add(log);

                node.getBody().statements().add(0, Utils.getMethodInvocationStmt(ast, null, "saveLog", new String[]{log}, new boolean[]{true}));

                return super.visit(node);
            }

            @Override
            public boolean visit(MethodInvocation node) {
                if (node.getName().getIdentifier().equals("saveLog")) {
                    return false;
                }

                SimpleName name = node.getName();
                String log = sourceFile + ":methodInvocation:" + name.getIdentifier() + " - line: " + unit.getLineNumber(node.getStartPosition());
                this.names.add(log);

                addLogCall(log, node);

                return super.visit(node);
            }

            @Override
            public boolean visit(Assignment node) {
                String name = node.getLeftHandSide().toString();
                String log = sourceFile + ":assignment:" + name + " - line: " + unit.getLineNumber(node.getStartPosition());
                this.names.add(log);

                addLogCall(log, node);
                
                return super.visit(node);
            }
            
            @Override
            public boolean visit(ReturnStatement node) {
                String name = node.getExpression().toString();
                String log = sourceFile + ":return:" + name + " - line: " + unit.getLineNumber(node.getStartPosition());
                this.names.add(log);
                addLogCall(log, node);
                
                return super.visit(node);
            }
            
            @Override
            public boolean visit(VariableDeclarationStatement node) {
                String name = node.fragments().toString();
                String log = sourceFile + ":variableDeclaration:" + name + " - line: " + unit.getLineNumber(node.getStartPosition());
                this.names.add(log);
                addLogCall(log, node);
                
                return super.visit(node);
            }
            
            
        };

        unit.accept(visitor);
        Set<String> currentLOG = ((SmartASTVisitor) visitor).getNames();

        result.put(sourceFile, currentLOG);

        if (currentLOG.size() > 0) {
            addSaveMethod(unit);
            unit.imports().add(Utils.getImportInstance(ast, "java.io", true));
        }

        return unit;
    }

}
