package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class AsmMethodBody extends AbstractMethodBody {
    private String codeString;

    public AsmMethodBody(String codeString) {
        Validate.notNull(codeString);
        this.codeString = codeString;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnedType)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("asm(");
        s.indent();
        s.println(codeString);
        s.unindent();
        s.print(")");
    }

    

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do (si j'ai bien compris)
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        s.print(prefix);
        s.print(codeString);
    }
}
