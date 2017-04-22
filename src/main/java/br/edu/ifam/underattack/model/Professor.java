package br.edu.ifam.underattack.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by erika.silva on 22/04/2017.
 */
@Entity
@DiscriminatorValue(value = "Professor")
public class Professor extends Usuario {

    public Professor(String login, String senha){
        super(login, senha);
    }

    public Professor() {
    }
}
