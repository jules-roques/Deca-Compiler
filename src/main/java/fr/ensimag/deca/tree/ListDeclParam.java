package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of params (e.g. exemple).
 * 
 * @author gl44
 * @date 15/01/2024
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        boolean premierParametre = true;
        for (AbstractDeclParam p : getList()) {
            if (premierParametre){
                premierParametre = false;
            }
            else{
                s.print(", ");
            }
            p.decompile(s);
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    void verifyListParam(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Signature sign) throws ContextualError {
        LOG.debug("verify listParam: start");
        for (AbstractDeclParam p : getList()) {
            p.verifyDeclParam(compiler, localEnv, currentClass, sign);
        }
        LOG.debug("verify listParam: end");
    }

}