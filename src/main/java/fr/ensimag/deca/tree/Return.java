package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl44
 * @date 01/01/2024
 */
public class Return extends AbstractInst {
    private AbstractExpr returnedExpr;


    public AbstractExpr getreturnedExpr() {
        return returnedExpr;
    }

    public Return(AbstractExpr returnedExpr) {
        Validate.notNull(returnedExpr);
        this.returnedExpr = returnedExpr;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        Type t = this.returnedExpr.verifyExpr(compiler, localEnv, currentClass);

        if (t.isVoid()) {
            throw new ContextualError("le type retourné ne peut pas être 'void' (règle 3.24)", getLocation());
        }

        if (!(returnType.sameType(t))) {
            throw new ContextualError("type de retour de méthode incorrect (règle 3.11)", getLocation());
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        returnedExpr.decompile(s);
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returnedExpr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnedExpr.prettyPrint(s, prefix, true);
    }

}
