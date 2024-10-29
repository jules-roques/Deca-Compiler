package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl44
 * @date 01/01/2024
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        AbstractLValue leftOperand = this.getLeftOperand();
        if ((leftOperand instanceof Identifier) && (localEnv.get(((Identifier)leftOperand).getName()) instanceof MethodDefinition)) {
            throw new ContextualError("Lvalue identifiant une méthode plutôt qu'une variable (règle 3.67)", getLocation());
        }
        Type t = leftOperand.verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(t.sameType(t2))) {
            throw new ContextualError("Assignation de mauvais type", getLocation());
        }
        this.verifyRValue(compiler, localEnv, currentClass, t);
        return t;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("L'affichage d'une assignation de comparaisons n'est pas supporté.");
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {

        DAddr addrIdentifierRetour = (DAddr) getLeftOperand().codeGenExpr(compiler);
        GPRegister addrResultAssign = getRightOperand().codeGenExpr(compiler).loadIfNotAlreadyInRegister(compiler);

        compiler.addInstruction(new STORE(addrResultAssign, addrIdentifierRetour));
        compiler.getMemory().freeAllRegisters();
        
        return (DVal) addrIdentifierRetour;
    }
}
