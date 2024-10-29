package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Utile à la gestion d'opérations sur les booléens
 */
public interface BooleanGestionInterface {

    /**
     * Sert à générér du code de branchement en fonction de la valeur de l'instruction
     * booléenne (soit Not(), soit BooleanLiteral(), soit AbstractOpBool(), soit AbstractOpCmp)
     * @param compiler
     * @param expectedValue
     * @param label
     */
    public abstract void codeGenBra(DecacCompiler compiler, boolean expectedValue, Label label);   
}
