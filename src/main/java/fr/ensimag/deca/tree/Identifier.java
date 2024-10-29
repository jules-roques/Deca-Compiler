package fr.ensimag.deca.tree;

// import static fr.ensimag.deca.tree.Tree.LOG;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Deca Identifier
 *
 * @author gl44
 * @date 01/01/2024
 */
public class Identifier extends AbstractIdentifier implements BooleanGestionInterface {
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if (localEnv == null) {
            throw new ContextualError("identificateur non déclaré (règle 0.1)", getLocation());
        }
        Type type;
        ExpDefinition varDef = localEnv.get(name);
        if (varDef == null) {
            type = this.verifyExpr(compiler, localEnv.getParentEnvironment(), currentClass);
            return type;
        } else {
            this.setDefinition(varDef);
            Type varType = varDef.getType();
            TypeDefinition typeDef = compiler.getEnvironmentTypes().defOfType(compiler.createSymbol(varType.toString()));
            type = typeDef.getType();
            this.setType(type);
            return type;
        }
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler, boolean typeMethod) throws ContextualError {
        
        if (this.name.toString() == "string") {
            throw new ContextualError("le type ne peut pas être 'string'", getLocation());
        }

        TypeDefinition typeDef = compiler.getEnvironmentTypes().defOfType(compiler.createSymbol(getName().toString()));
        
        if (typeDef == null) {
            throw new ContextualError("identificateur de type inconnu (règle 0.2)", getLocation());
        }

        this.setDefinition(typeDef);

        // définition du type
        Type type = typeDef.getType();
        
        if (!typeMethod) {
            if (type.toString() == "void") {
                throw new ContextualError("le type du champ ne peut pas être 'void' (règle 2.5)", getLocation());
            }
        }

        this.setType(type);
    
        return type;
    }

    public Type verifyClassExpr(DecacCompiler compiler) throws ContextualError {
        // Récupère la définition du type de classe
        TypeDefinition classDef = compiler.getEnvironmentTypes().defOfType(compiler.createSymbol(this.name.toString()));

        if (classDef == null) {
            throw new ContextualError("identificateur non déclaré (règle 1.3)", getLocation());
        }

        // récupère le type du type de classe
        Type classType = classDef.getType();
        
        // définition de la classe
        this.setDefinition(classDef);
        this.setType(classType);

        return classType;
    }

    public Type verifyMethodExpr(DecacCompiler compiler, ClassDefinition methodClass, ClassDefinition currentClass) 
            throws ContextualError {
        if (methodClass == null) {
            throw new ContextualError("identificateur non déclaré (règle 0.1)", getLocation());
        }
        Type type;
        EnvironmentExp localEnv = methodClass.getMembers();
        ExpDefinition methDef = localEnv.get(name);
        if (methDef == null) {
            type = this.verifyMethodExpr(compiler, methodClass.getSuperClass(), currentClass);
            return type;
        } else {
            this.setDefinition(methDef);
        Type methType = methDef.getType();
        TypeDefinition typeDef = compiler.getEnvironmentTypes().defOfType(compiler.createSymbol(methType.toString()));
        type = typeDef.getType();
        this.setType(type);
        return type;
        }        
    }


    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        return getExpDefinition().getOperand();
    }

    @Override
    public void codeGenPrint(DecacCompiler compiler) {
        DVal result = this.codeGenExpr(compiler);
        compiler.addInstruction(new LOAD(result, GPRegister.R1));
        this.addPrintInstruction(compiler);
    }

    @Override
    public void codeGenBra(DecacCompiler compiler, boolean expectedValue, Label label) {
        DAddr variableAddr = this.getExpDefinition().getOperand();
        compiler.addInstruction(new LOAD(variableAddr, GPRegister.R0));
        compiler.addInstruction(new CMP(new ImmediateInteger(0), GPRegister.R0));

        if (expectedValue) {
            compiler.addInstruction(new BNE(label));
        } else {
            compiler.addInstruction(new BEQ(label));
        }
    }

}
