package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 *
 * @author gl44
 * @date 01/01/2024
 */
public class New extends AbstractExpr {

    AbstractIdentifier className;

    public New(AbstractIdentifier className) {
        Validate.notNull(className);
        this.className = className;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // définition du type du new
        TypeDefinition typeDef = compiler.getEnvironmentTypes().defOfType(compiler.createSymbol(className.getName().toString()));
        if (!(typeDef instanceof ClassDefinition)) {
            throw new ContextualError("le type n’est pas celui d’une classe (règle 3.42)", getLocation());
        }
        Type type = typeDef.getType();
        this.verifyRValue(compiler, localEnv, currentClass, type);
        // définition de l'identifier
        className.verifyClassExpr(compiler);
        return type;
    }

    @Override
    public GPRegister codeGenExpr(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("L'affichage de nouveaux objets print(new XXX()) n'est pas encore implémenté.");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        className.decompile();
        s.print("()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, false);
    }

}

