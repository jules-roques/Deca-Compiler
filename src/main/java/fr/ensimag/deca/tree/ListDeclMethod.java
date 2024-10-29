package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of methods (e.g. exemple).
 * 
 * @author gl44
 * @date 15/01/2024
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod m : getList()) {
            m.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    void verifyListDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listMethod: start");
        currentClass.setNumberOfMethods(-1);
        for (AbstractDeclMethod m : getList()) {
            currentClass.incNumberOfMethods();
            m.verifyDeclMethod(compiler, currentClass.getMembers(), currentClass, currentClass.getNumberOfMethods());
        }
        LOG.debug("verify listMethod: end");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listMethodBody: start");
        for (AbstractDeclMethod m : getList()) {
            m.verifyDeclMethodBody(compiler, currentClass.getMembers(), currentClass);
        }
        LOG.debug("verify listMethodBody: end");
    }

}
