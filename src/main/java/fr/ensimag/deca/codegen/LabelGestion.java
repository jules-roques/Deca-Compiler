package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelGestion {
    private int numberEndLabels;
    private int numberElseLabels;
    private int numberCondLabels;
    private int numberDebutLabels;

    public LabelGestion() {
        this.numberEndLabels = 0;
        this.numberElseLabels = 0;
        this.numberCondLabels = 0;
        this.numberDebutLabels = 0;
    }

    public Label getNewEndLabel() {
        return new Label("E_Fin." + numberEndLabels++);
    }

    public Label getNewElseLabel() {
        return new Label("E_Sinon." + numberElseLabels++);
    }

    public Label getNewCondLabel() {
        return new Label("E_Cond." + numberCondLabels++);
    }

    public Label getNewDebutLabel() {
        return new Label("E_Debut." + numberDebutLabels++);
    }

    
}
