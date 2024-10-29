package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * Unary expression.
 *
 * @author gl44
 * @date 01/01/2024
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    public AbstractExpr getOperand() {
        return operand;
    }
    private AbstractExpr operand;
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }


    protected abstract String getOperatorName();
  
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(" + getOperatorName() + " ");
        operand.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }


    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        GPRegister destinationRegister = getOperand().codeGenExpr(compiler).loadIfNotAlreadyInRegister(compiler);
        codeGenUnaryExpr(compiler, destinationRegister);
        return destinationRegister;
    }

    /**
     * Add to the compiler the necessary instructions for an unary expression
     * Auteur: Jules
     * @param compiler
     * @param op
     */
    public abstract void codeGenUnaryExpr(DecacCompiler compiler, GPRegister op);
}
