package org.apache.tomcat.util.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tomcat.util.bcel.classfile.ArrayElementValue;
import org.apache.tomcat.util.bcel.classfile.ElementValue;

public class ArrayElementValueGen extends ElementValueGen
{
	// J5TODO: Should we make this an array or a list? A list would be easier to
	// modify ...
	private List /* ElementValueGen */evalues;

	public ArrayElementValueGen(ConstantPoolGen cp)
	{
		super(ARRAY, cp);
		evalues = new ArrayList();
	}

	public ArrayElementValueGen(int type, ElementValue[] datums,
			ConstantPoolGen cpool)
	{
		super(type, cpool);
		if (type != ARRAY)
			throw new RuntimeException(
					"Only element values of type array can be built with this ctor - type specified: " + type);
		this.evalues = new ArrayList();
		for (int i = 0; i < datums.length; i++)
		{
			evalues.add(datums[i]);
		}
	}

	/**
	 * Return immutable variant of this ArrayElementValueGen
	 */
	public ElementValue getElementValue()
	{
		ElementValue[] immutableData = new ElementValue[evalues.size()];
		int i = 0;
		for (Iterator iter = evalues.iterator(); iter.hasNext();)
		{
			ElementValueGen element = (ElementValueGen) iter.next();
			immutableData[i++] = element.getElementValue();
		}
		return new ArrayElementValue(type, immutableData, cpGen
				.getConstantPool());
	}

	/**
	 * @param value
	 * @param cpool
	 */
	public ArrayElementValueGen(ArrayElementValue value, ConstantPoolGen cpool,
			boolean copyPoolEntries)
	{
		super(ARRAY, cpool);
		evalues = new ArrayList();
		ElementValue[] in = value.getElementValuesArray();
		for (int i = 0; i < in.length; i++)
		{
			evalues.add(ElementValueGen.copy(in[i], cpool, copyPoolEntries));
		}
	}

	public void dump(DataOutputStream dos) throws IOException
	{
		dos.writeByte(type); // u1 type of value (ARRAY == '[')
		dos.writeShort(evalues.size());
		for (Iterator iter = evalues.iterator(); iter.hasNext();)
		{
			ElementValueGen element = (ElementValueGen) iter.next();
			element.dump(dos);
		}
	}

	public String stringifyValue()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (Iterator iter = evalues.iterator(); iter.hasNext();)
		{
			ElementValueGen element = (ElementValueGen) iter.next();
			sb.append(element.stringifyValue());
			if (iter.hasNext())
				sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	public List getElementValues()
	{
		return evalues;
	}

	public int getElementValuesSize()
	{
		return evalues.size();
	}

	public void addElement(ElementValueGen gen)
	{
		evalues.add(gen);
	}
}
