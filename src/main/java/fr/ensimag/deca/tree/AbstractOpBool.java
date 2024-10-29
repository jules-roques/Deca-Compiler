package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl44
 * @date 01/01/2024
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr implements BooleanGestionInterface {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (t.getName().toString() != "boolean" || t2.getName().toString() != "boolean") {
            throw new ContextualError("Opération booléennes sur des non-booléens", getLocation());
        }
        this.verifyRValue(compiler, localEnv, currentClass, t);
        return t;
    }

    @Override
    public GPRegister codeGenExpr(DecacCompiler compiler) {
        Label midLabel = compiler.getLabels().getNewEndLabel();
        Label endLabel = compiler.getLabels().getNewEndLabel();
        GPRegister destinationRegister = compiler.getMemory().getAndAllocAvailableRegister(compiler);

        // Si la veuleur de l'expression est false on saute
        this.codeGenBra(compiler, false, midLabel);

        // Instruction si l'expression est true
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), destinationRegister));
        compiler.addInstruction(new BRA(endLabel));

        // Instruction si l'expression est false
        compiler.addLabel(midLabel);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), destinationRegister));

        // Fin des branchements
        compiler.addLabel(endLabel);

        return destinationRegister;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("L'affichage d'opérations sur booléens n'est pas supporté.");
    }
}
