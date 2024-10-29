package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl44
 * @date 01/01/2024
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr implements BooleanGestionInterface {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        // if (t.getName().toString() == "boolean" || t2.getName().toString() == "boolean") {
        //     throw new ContextualError("Opération de comparaison sur des booléens", getLocation());
        // }
        TypeDefinition typeDef = compiler.getEnvironmentTypes().defOfType(compiler.createSymbol("boolean"));
        Type type = typeDef.getType();
        this.verifyRValue(compiler, localEnv, currentClass, type);
        return type;
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        GPRegister op1 = getLeftOperand().codeGenExpr(compiler).loadIfNotAlreadyInRegister(compiler);
        DVal op2 = getRightOperand().codeGenExpr(compiler);

        op2 = compiler.getMemory().restoreIfNecessary(compiler, op2);

        // Comparaison
        compiler.addInstruction(new CMP(op2, op1));

        // Libère op2 si c'est un registre et que aucune valeur n'est 
        // Dans la pile (autrement op esst R0)
        op2.freeIfTopGPRegister(compiler);

        // Générateur du bon outil de comparaison
        this.codeGenOpCmp(compiler, op1);

        return op1;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("L'affichage d'expressions de comparaisons n'est pas supporté.");
    }

    /**
     * Author: Jules
     * Génerates the code for the comparison (SEQ, SGT, SLT, ...)
     * @param compiler
     * @param registreRetour
     */
    public abstract void codeGenOpCmp(DecacCompiler compiler, GPRegister registreRetour);



    public void codeGenBra(DecacCompiler compiler, boolean expectedValue, Label label) {
        GPRegister op1 = getLeftOperand().codeGenExpr(compiler).loadIfNotAlreadyInRegister(compiler);
        DVal op2 = getRightOperand().codeGenExpr(compiler);

        // Comparaison
        compiler.addInstruction(new CMP(op2, op1));

        // Libère les deux registres utilisés poupr la comparaison
        compiler.getMemory().freeTopGPRegister(op2 instanceof GPRegister ? 2 : 1);

        // Générateur du bon outil de comparaison
        if (expectedValue) {
            codeGenBraInst(compiler, label);
        } else {
            codeGenBraOppositeInst(compiler, label);
        }
    }

    /**
     * Author: Jules
     * Génerates the code for the branchs (BEQ, BGT, BLT, ...)
     * @param compiler
     * @param registreRetour
     */
    public abstract void codeGenBraInst(DecacCompiler compiler, Label label);

    /**
     * Author: Jules
     * Génerates the code for the branchs of opposite comparison (EQ -> BNE, GT -> BLT, LE -> BGE, ...)
     * @param compiler
     * @param registreRetour
     */
    public abstract void codeGenBraOppositeInst(DecacCompiler compiler, Label label);

}
