package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl44
 * @date 01/01/2024
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        String operator = this.getOperatorName();
        if (operator == "%" && !(t.isInt() && t2.isInt())) {
            throw new ContextualError("Modulo sur des variables non entières", getLocation());
        }
        if (t.getName().toString() == "boolean" || t2.getName().toString() == "boolean") {
            throw new ContextualError("Opération arithmétique sur des booléens", getLocation());
        }
        if (t.getName().toString() == "float" && t2.getName().toString() == "float") {
            this.verifyRValue(compiler, localEnv, currentClass, t);
            return t;
        } else if (t.getName().toString() == "float"){
            this.verifyRValue(compiler, localEnv, currentClass, t);
            this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, t, true));
            return t;
        } else if (t2.getName().toString() == "float"){
            this.verifyRValue(compiler, localEnv, currentClass, t2);
            this.setLeftOperand(this.getLeftOperand().verifyRValue(compiler, localEnv, currentClass, t2, true));
            return t2;
        } else {
            this.verifyRValue(compiler, localEnv, currentClass, t2);
            return t;
        }
    }

    protected abstract String getOperatorName();

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {

        GPRegister op1 = getLeftOperand().codeGenExpr(compiler).loadIfNotAlreadyInRegister(compiler);
        DVal op2 = getRightOperand().codeGenExpr(compiler);

        op2 = compiler.getMemory().restoreIfNecessary(compiler, op2);

        // On ajoute l'instruction de l'opération dans compiler
        compiler.addInstruction(getInstruction(op2, op1));

        // Libère op2 si c'est un registre et que aucune valeur n'est 
        // Dans la pile (autrement dit op2 est R0)
        op2.freeIfTopGPRegister(compiler);

        return op1;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal result = this.codeGenExpr(compiler);
        compiler.addInstruction(new LOAD(result, GPRegister.R1));
        this.addPrintInstruction(compiler);
    }

    public abstract BinaryInstructionDValToReg getInstruction(DVal op1, GPRegister op2);
}
