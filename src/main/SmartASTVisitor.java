package main;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 *
 * @author Adson Macêdo
 * @author Thairam Michel
 */
public class SmartASTVisitor extends ASTVisitor{
    protected Set<String> names = new HashSet<>();

    public Set<String> getNames(){
        return this.names;
    }
}
