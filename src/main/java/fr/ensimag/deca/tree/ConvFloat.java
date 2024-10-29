package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl44
 * @date 01/01/2024
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type floatType = compiler.getEnvironmentTypes().defOfType(compiler.createSymbol("float")).getType();
        this.verifyRValue(compiler, localEnv, currentClass, floatType);
        return floatType;
    }

    @Override
    public void codeGenPrint(DecacCompiler compiler) {
        DVal result = this.codeGenExpr(compiler);
        compiler.addInstruction(new LOAD(result, GPRegister.R1));
        this.addPrintInstruction(compiler);
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    @Override
    public void codeGenUnaryExpr(DecacCompiler compiler, GPRegister op) {
        compiler.addInstruction(new FLOAT(op, op));
    }

}
