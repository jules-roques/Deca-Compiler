package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField f : getList()) {
            f.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    void verifyListDeclField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listDeclField: start");
        currentClass.setNumberOfFields(-1);
        for (AbstractDeclField f : getList()) {
            currentClass.incNumberOfFields();
            f.verifyDeclField(compiler, currentClass.getMembers(), currentClass, currentClass.getNumberOfFields());
        }
        LOG.debug("verify listDeclField: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListFieldInitialization(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listFieldInitialization: start");
        for (AbstractDeclField f : getList()) {
            f.verifyFieldInitialization(compiler, currentClass.getMembers(), currentClass);
        }
        LOG.debug("verify listFieldInitialization: end");
    }
}
