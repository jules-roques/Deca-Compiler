package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 *
 * @author gl44
 * @date 01/01/2024
 */
public class Not extends AbstractUnaryExpr implements BooleanGestionInterface {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        this.verifyRValue(compiler, localEnv, currentClass, t);
        return t;
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    public void codeGenUnaryExpr(DecacCompiler compiler, GPRegister op) {
        compiler.addInstruction(new SUB(new ImmediateInteger(1), op));
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("L'affichage d'opérations sur booléens n'est pas supporté.");
    }
    

    @Override
    public void codeGenBra(DecacCompiler compiler, boolean expectedValue, Label label) {
        ((BooleanGestionInterface) getOperand()).codeGenBra(compiler, !expectedValue, label);
    }
}
