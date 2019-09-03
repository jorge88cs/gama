/*********************************************************************************************
 *
 * 'StartSoundStatement.java, in plugin gama.extensions.sound, is part of the source code of the GAMA modeling
 * and simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package gama.extensions.sound;

import java.io.File;

import gama.extensions.sound.StartSoundStatement.StartSoundValidator;
import gama.processor.annotations.IConcept;
import gama.processor.annotations.ISymbolKind;
import gama.processor.annotations.GamlAnnotations.doc;
import gama.processor.annotations.GamlAnnotations.facet;
import gama.processor.annotations.GamlAnnotations.facets;
import gama.processor.annotations.GamlAnnotations.inside;
import gama.processor.annotations.GamlAnnotations.symbol;
import gama.common.interfaces.IAgent;
import gama.common.interfaces.IKeyword;
import gama.common.util.FileUtils;
import gama.runtime.exceptions.GamaRuntimeException;
import gama.runtime.scope.IScope;
import gaml.compilation.annotations.validator;
import gaml.compilation.interfaces.IDescriptionValidator;
import gaml.compilation.interfaces.ISymbol;
import gaml.descriptions.IDescription;
import gaml.expressions.IExpression;
import gaml.statements.AbstractStatementSequence;
import gaml.types.IType;

@symbol (
		name = IKeyword.START_SOUND,
		kind = ISymbolKind.SEQUENCE_STATEMENT,
		concept = { IConcept.SOUND },
		with_sequence = true,
		doc = @doc ("Starts playing a music file. The supported formats are aif, au, mp3, wav. One agent"))
@inside (
		kinds = { ISymbolKind.BEHAVIOR, ISymbolKind.SEQUENCE_STATEMENT })
@facets (
		value = { @facet (
				name = IKeyword.SOURCE,
				type = IType.STRING,
				optional = false,
				doc = @doc ("The path to music file. This path is relative to the path of the model.")),
				@facet (
						name = IKeyword.MODE,
						type = IType.ID,
						values = { IKeyword.OVERWRITE, IKeyword.IGNORE },
						optional = true,
						doc = @doc ("Mode of ")),
				@facet (
						name = IKeyword.REPEAT,
						type = IType.BOOL,
						optional = true,
						doc = @doc ("")) })
@validator (StartSoundValidator.class)
@SuppressWarnings ({ "rawtypes" })
@doc ("Allows to start the sound output")
public class StartSoundStatement extends AbstractStatementSequence {

	public static class StartSoundValidator implements IDescriptionValidator {

		/**
		 * Method validate()
		 * 
		 * @see msi.gaml.compilation.interfaces.IDescriptionValidator#validate(msi.gaml.descriptions.IDescription)
		 */
		@Override
		public void validate(final IDescription cd) {

		}
	}

	private final IExpression source;
	private final IExpression mode;
	private final IExpression repeat;

	private AbstractStatementSequence sequence = null;

	public StartSoundStatement(final IDescription desc) {
		super(desc);

		source = getFacet(IKeyword.SOURCE);
		mode = getFacet(IKeyword.MODE);
		repeat = getFacet(IKeyword.REPEAT);
	}

	@Override
	public void setChildren(final Iterable<? extends ISymbol> com) {
		sequence = new AbstractStatementSequence(description);
		sequence.setName("commands of " + getName());
		sequence.setChildren(com);
	}

	@Override
	public Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {

		final IAgent currentAgent = scope.getAgent();

		final GamaSoundPlayer soundPlayer = SoundPlayerBroker.getInstance().getSoundPlayer(currentAgent);
		final String soundFilePath = FileUtils.constructAbsoluteFilePath(scope, (String) source.value(scope), false);

		if (soundPlayer != null) {
			soundPlayer.play(scope, new File(soundFilePath),
					mode != null ? (String) mode.value(scope) : GamaSoundPlayer.OVERWRITE_MODE,
					repeat != null ? (Boolean) repeat.value(scope) : false);
		} else {
			// DEBUG.LOG("No more player in pool!");
		}

		if (sequence != null) {
			scope.execute(sequence, currentAgent, null);
		}

		return null;
	}
}
