package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * read...() statement.
 *
 * @author gl44
 * @date 01/01/2024
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    @Override
    public GPRegister codeGenExpr(DecacCompiler compiler) {
        // Place le nombre lu dans R1
        this.addReadInstruction(compiler);

        // Place le contenu de R1 dans un nouveau registre
        GPRegister destinationRegister = compiler.getMemory().getAndAllocAvailableRegister(compiler);
        compiler.addInstruction(new LOAD(GPRegister.R1, destinationRegister));

        return destinationRegister;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal result = this.codeGenExpr(compiler);
        compiler.addInstruction(new LOAD(result, GPRegister.R1));
        this.addPrintInstruction(compiler);
    }

    public abstract void addReadInstruction(DecacCompiler compiler);



}
