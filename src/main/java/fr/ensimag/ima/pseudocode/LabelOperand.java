package fr.ensimag.ima.pseudocode;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;

/**
 * Label used as operand
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class LabelOperand extends DVal {
    public Label getLabel() {
        return label;
    }

    private Label label;
    
    public LabelOperand(Label label) {
        super();
        Validate.notNull(label);
        this.label = label;
    }

    public GPRegister loadIfNotAlreadyInRegister(DecacCompiler compiler) {
        throw new DecacInternalError("On ne peut pas stocker de label dans un registre");
    }

    @Override
    public void freeIfTopGPRegister(DecacCompiler compiler) {
        // Nothing
    }

    @Override
    public String toString() {
        return label.toString();
    }

}
