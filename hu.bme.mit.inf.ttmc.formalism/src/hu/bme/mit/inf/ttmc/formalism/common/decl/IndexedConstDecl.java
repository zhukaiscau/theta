package hu.bme.mit.inf.ttmc.formalism.common.decl;

import hu.bme.mit.inf.ttmc.core.decl.ConstDecl;
import hu.bme.mit.inf.ttmc.core.type.Type;

public interface IndexedConstDecl<DeclType extends Type> extends ConstDecl<DeclType> {

	public VarDecl<DeclType> getVarDecl();

	public int getIndex();

}
