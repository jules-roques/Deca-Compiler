package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl44
 * @date 01/01/2024
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

    public void verifySignature(MethodDefinition methDef) throws ContextualError {
        Signature methSign = methDef.getSignature();
        if (this.size() != methSign.size()) {
            throw new ContextualError("Nombre incorrect de paramètres dans un appel de méthode (règle 3.74)", methDef.getLocation());
        }
        for (int i = 0; i < this.size(); i++) {
            if (this.paramNumber(i).isClass() && methSign.paramNumber(i).isClass()) {
                if(!((ClassType)this.paramNumber(i)).isSubClassOf((ClassType)methSign.paramNumber(i))) {
                    throw new ContextualError("Appel de méthode avec des paramètres incompatibles (règle 3.71)", methDef.getLocation());
                }
            } else if (!(this.paramNumber(i).sameType(methSign.paramNumber(i)))) {
                throw new ContextualError("Appel de méthode avec des paramètres incompatibles (règle 3.71)", methDef.getLocation());
            }
            
            
            
            // if (!(this.paramNumber(i).sameType(methSign.paramNumber(i)))) {
            //     throw new ContextualError("Appel de méthode avec des paramètres incompatibles (règle 3.71)", methDef.getLocation());
            // }
        }
    }

}
