package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * @author gl44
 * @date 01/01/2024
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type type = expression.verifyExpr(compiler, localEnv, currentClass);
        if (expression instanceof Identifier) {
            Definition exprDef = ((Identifier)expression).getDefinition();
            if (exprDef instanceof MethodDefinition) {
                throw new ContextualError("Lvalue identifiant une méthode plutôt qu'une variable (règle 3.67)", getLocation());
            }
        }
        if (!(type.sameType(t))) {
            throw new ContextualError("Mauvais type à l'initalisation (règle 3.28)", getLocation());
        }

    }



    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        expression.decompile(s);  
        }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }

    @Override
    public void codeGenInitialization(DecacCompiler compiler, ExpDefinition varDef) {

        Register tempRegister = expression.codeGenExpr(compiler).loadIfNotAlreadyInRegister(compiler);
        
        compiler.addInstruction(new STORE(tempRegister, varDef.getOperand()));
        compiler.getMemory().freeAllRegisters();
    }
}
