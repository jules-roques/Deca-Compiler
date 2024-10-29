package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.OPP;

/**
 * @author gl44
 * @date 01/01/2024
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
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
        return "-";
    }

    @Override
    public void codeGenUnaryExpr(DecacCompiler compiler, GPRegister op) {
        compiler.addInstruction(new OPP(op, op));
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal result = this.codeGenExpr(compiler);
        compiler.addInstruction(new LOAD(result, GPRegister.R1));
        this.addPrintInstruction(compiler);
    }
}
