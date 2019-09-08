/*******************************************************************************************************
 *
 * gaml.statements.test.AssertionSummary.java, in plugin gama.core,
 * is part of the source code of the GAMA modeling and simulation platform (v. 1.8)
 * 
 * (c) 2007-2018 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package gaml.statements.test;

import java.util.Collections;
import java.util.Map;

import gaml.operators.Strings;

/**
 * A summary of assert statements. Conrary to other summaries, they possess a state
 * 
 * @author drogoul
 *
 */
public class AssertionSummary extends AbstractSummary<AssertStatement> {
	private TestState state = TestState.NOT_RUN;
	public final long timeStamp;

	public AssertionSummary(final AssertStatement a) {
		super(a);
		timeStamp = System.currentTimeMillis();
	}

	@Override
	public void setState(final TestState s) {
		state = s;
	}

	@Override
	public void reset() {
		super.reset();
		state = TestState.NOT_RUN;
	}

	@Override
	public TestState getState() {
		return state;
	}

	@Override
	protected void printFooter(final StringBuilder sb) {
		sb.append(Strings.LN);
	}

	@Override
	public Map<String, ? extends AbstractSummary<?>> getSummaries() {
		return Collections.EMPTY_MAP;
	}

	@Override
	public int countTestsWith(final TestState state) {
		return 0;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public long getTimeStamp() {
		return timeStamp;
	}

}