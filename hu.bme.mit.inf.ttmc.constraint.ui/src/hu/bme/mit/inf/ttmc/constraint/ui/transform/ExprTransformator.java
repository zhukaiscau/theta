package hu.bme.mit.inf.ttmc.constraint.ui.transform;

import hu.bme.mit.inf.ttmc.constraint.expr.Expr;
import hu.bme.mit.inf.ttmc.constraint.model.Expression;
import hu.bme.mit.inf.ttmc.constraint.type.Type;

public interface ExprTransformator {

	public Expr<? extends Type> transform(Expression expression);

}
