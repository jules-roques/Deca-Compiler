package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl44
 * @date 01/01/2024
 */
public class BooleanLiteral extends AbstractExpr implements BooleanGestionInterface {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        TypeDefinition typeDef = compiler.getEnvironmentTypes().defOfType(compiler.createSymbol("boolean"));
        Type type = typeDef.getType();
        this.verifyRValue(compiler, localEnv, currentClass, type);
        return type;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("L'affichage de booléens n'est pas supporté.");
    }

    @Override
    public GPRegister codeGenExpr(DecacCompiler compiler) {
        GPRegister destinationRegister = compiler.getMemory().getAndAllocAvailableRegister(compiler);
        compiler.addInstruction(new LOAD(value ? 1 : 0, destinationRegister));
        return destinationRegister;
    }

    @Override
    public void codeGenBra(DecacCompiler compiler, boolean expectedValue, Label label) {
        if (this.value && expectedValue) {
            compiler.addInstruction(new BRA(label));
        } else if (!this.value) {
            Not notTrue = new Not(new BooleanLiteral(true));
            notTrue.codeGenBra(compiler, expectedValue, label);
        }
    }
}
