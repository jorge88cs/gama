/**
 * Created by bgaudou, 18 July 2018
 *
 */

package gama.extensions.serialize.gaml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

import gama.extensions.serialize.factory.StreamConverter;
import gama.extensions.serialize.gamaType.converters.ConverterScope;
import gaml.statements.Arguments;
import gaml.statements.CreateStatement;
import gaml.types.IType;

import gama.common.interfaces.ICreateDelegate;
import gama.metamodel.agent.SavedAgent;
import gama.runtime.scope.IScope;
import gaml.types.Types;

/**
 * Class CreateFromSavecSimulationDelegate.
 *
 * @author bgaudou
 * @since 18 July 2018
 *
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CreateFromSavedSimulationDelegate implements ICreateDelegate {

	/**
	 * Method acceptSource()
	 * 
	 * @see msi.gama.common.interfaces.ICreateDelegate#acceptSource(IScope, java.lang.Object)
	 */
	@Override
	public boolean acceptSource(IScope scope, final Object source) {
		return source instanceof GamaSavedSimulationFile;
	}

	/**
	 * @see msi.gama.common.interfaces.ICreateDelegate#createFrom(msi.gama.runtime.scope.IScope,
	 *      java.util.List, int, java.lang.Object)
	 */
	@Override
	public boolean createFrom(final IScope scope, final List<Map<String, Object>> inits, final Integer max,
			final Object source, final Arguments init, final CreateStatement statement) {
		final GamaSavedSimulationFile file = (GamaSavedSimulationFile) source;

		final ConverterScope cScope = new ConverterScope(scope);
		final XStream xstream = StreamConverter.loadAndBuild(cScope);

		String stringFile = file.getBuffer().get(0);
		final SavedAgent saveAgt = (SavedAgent) xstream.fromXML(stringFile);
		
		HashMap mapSavedAgt = new HashMap<String, Object>();
		mapSavedAgt.put("SavedAgent", saveAgt);
		
		inits.add(mapSavedAgt);
		
		return true;
	}


	/**
	 * Method fromFacetType()
	 * 
	 * @see msi.gama.common.interfaces.ICreateDelegate#fromFacetType()
	 */
	@Override
	public IType fromFacetType() {
		return Types.FILE;
	}
}
