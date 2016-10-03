package me.lucko.luckperms.standalone.util.form;

public class FormResult {

	private FormResultType type;
	private Object[] result;

	public FormResult(FormResultType type, Object[] result) {
		this.type = type;
		this.result = result;
	}

	public FormResultType getType() {
		return type;
	}

	public Object[] getResult() {
		return result;
	}
}
