import java.util.HashMap;
import java.util.List;

import japa.parser.ast.body.*;
import japa.parser.ast.visitor.*;

public class VariableVisitor extends VoidVisitorAdapter {

	HashMap<String, String> fieldAndType = new HashMap<String, String>();

	public VariableVisitor() {

	}

	public void visit(FieldDeclaration fields, Object object) {

		List<VariableDeclarator> variableList = fields.getVariables();

		for (VariableDeclarator variable : variableList) {
			fieldAndType.put(fields.getType().toString(), variable.getId()
					.getName());
		}

	}

	public HashMap<String, String> returnFieldAndType() {

		return fieldAndType;

	}

}
