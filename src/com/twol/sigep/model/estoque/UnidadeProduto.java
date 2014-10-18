package com.twol.sigep.model.estoque;

public enum UnidadeProduto {
    UND, KG, G, M, L;
    
    public String getName(){
    	return this.name().replace("_", " ");
    }
}
