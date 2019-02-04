package br.gov.ba.pge.validadoc.api.enums;

public enum EnumRelations {

	CONTENT_MEDIA("http://identifiers.emc.com/linkrel/content-media"),
	CONTENTS_CONTENTS("http://identifiers.emc.com/linkrel/content-media"),
	SELF("self"), 
	CABINETS("http:// identifiers.emc.com/linkrel/cabinets"),
	DQL("http:// identifiers.emc.com/linkrel/dql");

	private final String relation;

	private EnumRelations(String relation) {
        this.relation = relation;
    }

	public String getRelation() {
		return relation;
	}
}
