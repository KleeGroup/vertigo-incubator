package snowblood.gen.domain;

import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.stereotype.DtDefinition;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

/**
 * Attention cette classe est générée automatiquement !
 * Objet de données Jobexecution
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "JOBEXECUTION")
@DtDefinition
public final class Jobexecution implements DtObject {

	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	private Long joeId;
	private String parametres;
	private java.util.Date debut;
	private java.util.Date fin;
	private String serveur;
	private String rapport;
	private String logs;
	private String data;
	private Long jodId;
	private snowblood.gen.domain.Jobdefinition definition;
	// Enumerations
	private String triggerCd;
	private String statusCd;

	/**
	 * Champ : PRIMARY_KEY.
	 * Récupère la valeur de la propriété 'JOE_ID'.
	 * @return Long joeId <b>Obligatoire</b>
	 */
	@javax.persistence.Id
	@javax.persistence.SequenceGenerator(name = "sequence", sequenceName = "SEQ_JOBEXECUTION")
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO, generator = "sequence")
	@javax.persistence.Column(name = "JOE_ID")
	@Field(domain = "DO_IDENTIFIANT", type = "PRIMARY_KEY", notNull = true, label = "JOE_ID")
	public Long getJoeId() {
		return joeId;
	}

	/**
	 * Champ : PRIMARY_KEY.
	 * Définit la valeur de la propriété 'JOE_ID'.
	 * @param joeId Long <b>Obligatoire</b>
	 */
	public void setJoeId(final Long joeId) {
		this.joeId = joeId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Paramètres'.
	 * @return String parametres
	 */
	@javax.persistence.Column(name = "PARAMETRES")
	@Field(domain = "DO_COMMENTAIRE", label = "Paramètres")
	public String getParametres() {
		return parametres;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Paramètres'.
	 * @param parametres String
	 */
	public void setParametres(final String parametres) {
		this.parametres = parametres;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Début'.
	 * @return java.util.Date debut
	 */
	@javax.persistence.Column(name = "DEBUT")
	@Field(domain = "DO_DATE_HEURE", label = "Début")
	public java.util.Date getDebut() {
		return debut;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Début'.
	 * @param debut java.util.Date
	 */
	public void setDebut(final java.util.Date debut) {
		this.debut = debut;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Fin'.
	 * @return java.util.Date fin
	 */
	@javax.persistence.Column(name = "FIN")
	@Field(domain = "DO_DATE_HEURE", label = "Fin")
	public java.util.Date getFin() {
		return fin;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Fin'.
	 * @param fin java.util.Date
	 */
	public void setFin(final java.util.Date fin) {
		this.fin = fin;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Serveur'.
	 * @return String serveur
	 */
	@javax.persistence.Column(name = "SERVEUR")
	@Field(domain = "DO_LIBELLE_COURT", label = "Serveur")
	public String getServeur() {
		return serveur;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Serveur'.
	 * @param serveur String
	 */
	public void setServeur(final String serveur) {
		this.serveur = serveur;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Rapport'.
	 * @return String rapport
	 */
	@javax.persistence.Column(name = "RAPPORT")
	@Field(domain = "DO_COMMENTAIRE", label = "Rapport")
	public String getRapport() {
		return rapport;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Rapport'.
	 * @param rapport String
	 */
	public void setRapport(final String rapport) {
		this.rapport = rapport;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Logs'.
	 * @return String logs
	 */
	@javax.persistence.Column(name = "LOGS")
	@Field(domain = "DO_LIBELLE_LONG", label = "Logs")
	public String getLogs() {
		return logs;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Logs'.
	 * @param logs String
	 */
	public void setLogs(final String logs) {
		this.logs = logs;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Data'.
	 * @return String data
	 */
	@javax.persistence.Column(name = "DATA")
	@Field(domain = "DO_LIBELLE_LONG", label = "Data")
	public String getData() {
		return data;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Data'.
	 * @param data String
	 */
	public void setData(final String data) {
		this.data = data;
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Définition'.
	 * @return Long jodId <b>Obligatoire</b>
	 */
	@javax.persistence.Column(name = "JOD_ID")
	@Field(domain = "DO_IDENTIFIANT", type = "FOREIGN_KEY", notNull = true, label = "Définition")
	public Long getJodId() {
		return jodId;
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Définition'.
	 * @param jodId Long <b>Obligatoire</b>
	 */
	public void setJodId(final Long jodId) {
		this.jodId = jodId;
	}

	// Association : Tmp import srj rapport non navigable
	/**
	 * Association : Définition.
	 * @return fr.justice.isis.domain.tourdecontrole.Jobdefinition
	 */
	@javax.persistence.Transient
	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_JOE_JOD",
			fkFieldName = "JOD_ID",
			primaryDtDefinitionName = "DT_JOBDEFINITION",
			primaryIsNavigable = true,
			primaryRole = "Definition",
			primaryLabel = "Définition",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DT_JOBEXECUTION",
			foreignIsNavigable = false,
			foreignRole = "Execution",
			foreignLabel = "Exécution",
			foreignMultiplicity = "0..*"
			)
			public snowblood.gen.domain.Jobdefinition getDefinition() {
		final io.vertigo.dynamo.domain.model.URI<snowblood.gen.domain.Jobdefinition> fkURI = getDefinitionURI();
		if (fkURI == null) {
			return null;
		}
		//On est toujours dans un mode lazy. On s'assure cependant que l'objet associé n'a pas changé
		if (definition != null) {
			// On s'assure que l'objet correspond à la bonne clé
			final io.vertigo.dynamo.domain.model.URI<snowblood.gen.domain.Jobdefinition> uri;
			uri = new io.vertigo.dynamo.domain.model.URI<>(io.vertigo.dynamo.domain.util.DtObjectUtil.findDtDefinition(definition), io.vertigo.dynamo.domain.util.DtObjectUtil.getId(definition));
			if (!fkURI.toURN().equals(uri.toURN())) {
				definition = null;
			}
		}
		if (definition == null) {
			definition = io.vertigo.core.Home.getComponentSpace().resolve(io.vertigo.dynamo.persistence.PersistenceManager.class).getBroker().get(fkURI);
		}
		return definition;
	}

	/**
	 * Retourne l'URI: Définition.
	 * @return URI de l'association
	 */
	@javax.persistence.Transient
	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_JOE_JOD",
			fkFieldName = "JOD_ID",
			primaryDtDefinitionName = "DT_JOBDEFINITION",
			primaryIsNavigable = true,
			primaryRole = "Definition",
			primaryLabel = "Définition",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DT_JOBEXECUTION",
			foreignIsNavigable = false,
			foreignRole = "Execution",
			foreignLabel = "Exécution",
			foreignMultiplicity = "0..*"
			)
			public io.vertigo.dynamo.domain.model.URI<snowblood.gen.domain.Jobdefinition> getDefinitionURI() {
		return DtObjectUtil.createURI(this, "A_JOE_JOD", snowblood.gen.domain.Jobdefinition.class);
	}

	// ************************************************************************
	// Trigerring event : [S]cheduled, [E]xternal, [M]anual or [R]ule-based.
	// Checked against ActivityStatus enumeration.

	/**
	 * Champ : triggerCd
	 * Returns triggering event code.
	 * @return String triggerCd
	 */
	@javax.persistence.Column(name = "TRIGGER_CD")
	@Field(domain = "DO_CODE", label = "Triggering event")
	public String getTriggerCd() {
		return triggerCd;
	}

	/**
	 * Champ : triggerCd
	 * Returns triggering event (from enumeration).
	 * @return ActivityTrigger
	 */
	public ActivityTrigger getTrigger() {
		return ActivityTrigger.valueOf(triggerCd);
	}

	/**
	 * Champ : triggerCd
	 * Sets triggering event.
	 * @param triggerEvent ActivityTrigger
	 */
	public void setTrigger(final ActivityTrigger triggerEvent) {
		triggerCd = triggerEvent.getCode();
	}

	/**
	 * Champ : triggerCd
	 * Sets triggering event.
	 * @param triggerCode String
	 */
	public void setTriggerCd(final String triggerCode) {
		triggerCd = triggerCode;
	}

	// ************************************************************************
	// Status : [SUC]cess, [PAR]tial success, [RUN]ning or [FAI]lure
	// Checked against ActivityStatus enumeration.

	/**
	 * Champ : statusCd
	 * Returns data mode code.
	 * @return String jdcCd
	 */
	@javax.persistence.Column(name = "STATUS_CD")
	@Field(domain = "DO_CODE", label = "Status")
	public String getStatusCd() {
		return statusCd;
	}

	/**
	 * Champ : statusCd
	 * Returns status (from enumeration).
	 * @return ActivityDataMode
	 */
	public ActivityStatus getStatus() {
		return ActivityStatus.valueOf(statusCd);
	}

	/**
	 * Champ : statusCd
	 * Sets status.
	 * @param status ActivityStatus
	 */
	public void setStatus(final ActivityStatus status) {
		statusCd = status.getCode();
	}

	/**
	 * Champ : statusCd
	 * Sets status.
	 * @param statusCode String
	 */
	public void setStatusCd(final String statusCode) {
		statusCd = statusCode;
	}

	// ************************************************************************

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
