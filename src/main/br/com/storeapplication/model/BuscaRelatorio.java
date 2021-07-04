package br.com.storeapplication.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class BuscaRelatorio implements Serializable {

	private Date periodoinicial;
	private Date periodofinal;

	public Date getPeriodoinicial() {
		return periodoinicial;
	}

	public void setPeriodoinicial(Date periodoinicial) {
		this.periodoinicial = periodoinicial;
	}

	public Date getPeriodofinal() {
		return periodofinal;
	}

	public void setPeriodofinal(Date periodofinal) {
		this.periodofinal = periodofinal;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BuscaRelatorio that = (BuscaRelatorio) o;
		return Objects.equals(periodoinicial, that.periodoinicial) &&
				Objects.equals(periodofinal, that.periodofinal);
	}

	@Override
	public int hashCode() {
		return Objects.hash(periodoinicial, periodofinal);
	}

	@Override
	public String toString() {
		return "BuscaRelatorio{" +
				"periodoinicial=" + periodoinicial +
				", periodofinal=" + periodofinal +
				'}';
	}
}